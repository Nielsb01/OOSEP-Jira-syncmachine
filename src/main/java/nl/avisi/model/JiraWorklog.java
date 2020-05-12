package nl.avisi.model;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.dto.WorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for retrieving and creating worklogs on the specified Jira server through the Tempo API with HTTP requests
 */

@Default
public class JiraWorklog {

    /**
     * base URL where the Jira server of the client is being hosted
     */
    private String clientUrl;

    /**
     * base URL where the Jira server of Avisi is being hosted
     */
    private String avisiUrl;

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest<BasicAuth> request;

    /**
     * Contains information for the authentication required to make a HTTP request
     */
    private BasicAuth basicAuth;

    /**
     * is used to read the necessary property information
     */
    private JiraSynchronisationProperties jiraSynchronisationProperties;

    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = String.format("%s/rest/tempo-timesheets/4/worklogs/search", clientUrl);
    }

    public void setAvisiUrl(String avisiUrl) {
        this.avisiUrl = String.format("%s/rest/tempo-timesheets/4/worklogs", avisiUrl);
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    /**
     * @param worklogRequestDTO Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return List of all worklogs that were retrieved from the client server between the two given dates for the specified workers.
     */
    public List<WorklogDTO> retrieveWorklogsFromClientServer(WorklogRequestDTO worklogRequestDTO) {
        setClientUrl(jiraSynchronisationProperties.getOriginUrl());

        HttpResponse<JsonNode> jsonWorklogs = requestWorklogs(worklogRequestDTO);

        if (jsonWorklogs.getBody() == null || !jsonWorklogs.getBody().isArray()) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = jsonWorklogs.getBody().getArray();

        return createWorklogDTOs(jsonArray);
    }

    /**
     * @param jsonArray All retrieved worklogs in jsonArray form.
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    private List<WorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
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
     * @param requestBody Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return httpResponse containing the worklogs in JsonNode form.
     */
    private HttpResponse<JsonNode> requestWorklogs(WorklogRequestDTO requestBody) {
        setBasicAuth(new BasicAuth()
                .setPassword(jiraSynchronisationProperties.getAdminPassword())
                .setUsername(jiraSynchronisationProperties.getAdminUsername()));
        request.setAuthentication(basicAuth);

        return request.post(clientUrl, requestBody);
    }

    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link WorklogDTO}.
     * the standard comment of the {@link WorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from client Jira-server.
     * @return A map of worklogDTO's with their corresponding status codes after being posted.
     */
    public Map createWorklogsOnAvisiServer(List<WorklogDTO> worklogs) {
        setAvisiUrl(jiraSynchronisationProperties.getDestinationUrl());

        Map<WorklogDTO, Integer> responseCodes = new HashMap<>();

        for (WorklogDTO worklog : worklogs) {
            HttpResponse<JsonNode> response = request.post(avisiUrl, worklog);
            responseCodes.put(worklog, response.getStatus());

        }

        return responseCodes;
    }
}
