package nl.avisi;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Default
public class RetrieveData {

    /**
     * base URL where the Jira server is being hosted
     */
    private String url;

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest request;

    /**
     * Contains information for the authentication required to make a HTTP request
     */
    private BasicAuth basicAuth;


    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    public void setUrl(String url) {
        this.url = url + "rest/tempo-timesheets/4/worklogs/search";
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    /**
     *
     * @param from Date to be used in retrieving the worklogs from a certain point in time.
     * @param to Date to be used in retrieving the worklogs to a certain point in time.
     * @param workers The workers that will have their worklogs retrieved.
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    public List<WorklogDTO> retrieveWorklogs(String from, String to, List<String> workers) {
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO().setFrom(from).setTo(to).setWorker(workers);
        HttpResponse<JsonNode> JSONWorklogs = requestWorklogs(worklogRequestDTO);

        if (JSONWorklogs.getBody() == null || !JSONWorklogs.getBody().isArray()) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = JSONWorklogs.getBody().getArray();

        return createWorklogs(jsonArray);
    }

    /**
     *
     * @param jsonArray All retrieved worklogs in jsonArray form.
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    private List<WorklogDTO> createWorklogs(JSONArray jsonArray) {
        List<WorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                String worker = jsonObject.getString("worker");
                String started = jsonObject.getString("started");
                String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
                int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");

                worklogs.add(new WorklogDTO().setWorker(worker).setStarted(started).setOriginTaskId(originTaskId).setTimeSpentSeconds(timeSpentSeconds));
            } catch (JSONException e) {
                return new ArrayList<>();
            }
        }

        return worklogs;
    }

    /**
     *
     * @param requestBody Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return httpResponse containing the worklogs in JsonNode form.
     */
    private HttpResponse<JsonNode> requestWorklogs(WorklogRequestDTO requestBody) {
        request.setAuthentication(basicAuth);

        return request.post(url, requestBody);
    }
}
