package nl.avisi;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;

import javax.inject.Inject;
import javax.json.JsonArray;
import java.util.ArrayList;
import java.util.List;


public class RetrieveData {

    private String url;
    private IRequest request;
    private BasicAuth basicAuth;

    @Inject
    public void setRequest(IRequest request) {
        this.request = request;
    }

    public RetrieveData() {
    }

    public RetrieveData(String url, BasicAuth basicAuth) {
        this.url = url;
        this.basicAuth = basicAuth;
    }

    /**
     * @return Contains all the worklogs that were retrieved within the given date range.
     */
    public List<WorklogDTO> retrieveWorklogs(String from, String to, List<String> workers) {


        WorklogRequestBody worklogRequestBody = new WorklogRequestBody(from, to, workers);
        HttpResponse<JsonNode> JSONWorklogs = requestWorklogs(worklogRequestBody);

        List<WorklogDTO> worklogs = new ArrayList<>();
        JSONArray jsonArray = JSONWorklogs.getBody().getArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String worker = jsonObject.getString("worker");
            String started = jsonObject.getString("started");
            String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
            int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");

            worklogs.add(new WorklogDTO(worker, started, timeSpentSeconds, originTaskId));
        }
        return worklogs;
    }


    private HttpResponse<JsonNode> requestWorklogs(WorklogRequestBody requestBody) {
        request.setAuthentication(basicAuth);

        return request.post(url, requestBody);

    }

}
