package nl.avisi;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Default
public class RetrieveData {

    public RetrieveData() {
    }

    private String url;
    private IRequest request;
    private BasicAuth basicAuth;

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }



    public List<WorklogDTO> retrieveWorklogs(String from, String to, List<String> workers) {


        WorklogRequestBody worklogRequestBody = new WorklogRequestBody().setFrom(from).setTo(to).setWorker(workers);
        HttpResponse<JsonNode> JSONWorklogs = requestWorklogs(worklogRequestBody);


        if (JSONWorklogs.getBody() == null) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = JSONWorklogs.getBody().getArray();

        return createWorklogs(jsonArray);
    }

    private List<WorklogDTO> createWorklogs(JSONArray jsonArray) {
        List<WorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String worker = jsonObject.getString("worker");
            String started = jsonObject.getString("started");
            String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
            int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");

            worklogs.add(new WorklogDTO().setWorker(worker).setStarted(started).setOriginTaskId(originTaskId).setTimeSpentSeconds(timeSpentSeconds));
        }

        return worklogs;
    }


    private HttpResponse<JsonNode> requestWorklogs(WorklogRequestBody requestBody) {
        request.setAuthentication(basicAuth);

        return request.post(url, requestBody);

    }



}
