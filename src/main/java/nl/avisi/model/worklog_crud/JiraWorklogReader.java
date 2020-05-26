package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class JiraWorklogReader {

    private TempoInterface tempoInterface;

    @Inject
    public void setTempoInterface(TempoInterface tempoInterface) {
        this.tempoInterface = tempoInterface;
    }

    /**
     * Makes a HTTP post request to the tempo api containing the Jira user keys of the workers and a date range to
     * retrieve the worklogs matching that query.
     *
     * @param worklogRequestDTO Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return List of all worklogs that were retrieved from the client server between the two given dates for the specified workers.
     */
    public List<OriginWorklogDTO> readWorklogsFromOriginServer(WorklogRequestDTO worklogRequestDTO) {
        HttpResponse<JsonNode> worklogs = tempoInterface.requestOriginJiraWorklogs(worklogRequestDTO);

        if (worklogs.getBody() == null || !worklogs.getBody().isArray()) {
            return new ArrayList<>();
        }

        return createWorklogDTOs(worklogs.getBody().getArray());
    }

    private List<OriginWorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
        List<OriginWorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            worklogs.add(
                    new OriginWorklogDTO(
                            jsonObject.getString("worker"),
                            jsonObject.getString("started"),
                            jsonObject.getInt("timeSpentSeconds"),
                            jsonObject.getJSONObject("issue").getString("accountKey"),
                            jsonObject.getInt("tempoWorklogId"))
            );
        }

        return worklogs;
    }
}
