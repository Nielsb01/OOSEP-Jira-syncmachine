package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return Map of all worklogs that were retrieved from the client server between the two given dates for the specified workers.
     */
    public Map<Integer, DestinationWorklogDTO> retrieveWorklogsFromOriginServer(WorklogRequestDTO worklogRequestDTO) {

        HttpResponse<JsonNode> jsonWorklogs = tempoInterface.requestOriginJiraWorklogs(worklogRequestDTO);

        if (jsonWorklogs.getBody() == null || !jsonWorklogs.getBody().isArray()) {
            return new HashMap<>();
        }

        JSONArray jsonArray = jsonWorklogs.getBody().getArray();

        return createWorklogDTOs(jsonArray);
    }

    /**
     * Creates WorklogDTOs from the passed in JSONArray and puts them in a list
     *
     * @param jsonArray All retrieved worklogs in jsonArray form.
     * @return Map of all worklogs that were retrieved between the two given dates for the specified workers with their
     * corresponding worklogId.
     */
    private Map<Integer, DestinationWorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
        Map<Integer, DestinationWorklogDTO> worklogs = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                String worker = jsonObject.getString("worker");
                String started = jsonObject.getString("started");
                String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
                int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");
                int worklogId = jsonObject.getInt("tempoWorklogId");

                worklogs.put(worklogId, new DestinationWorklogDTO(worker, started, timeSpentSeconds, originTaskId));

            } catch (JSONException e) {
                return new HashMap<>();
            }
        }

        return worklogs;
    }
}
