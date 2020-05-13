package nl.avisi.model;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.inject.Inject;
import javax.json.Json;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for retrieving and creating worklogs on the specified Jira server through the Tempo API with HTTP requests
 */
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

    private IUserDAO userDAO;

    private IWorklogDAO worklogDAO;

    @Inject
    public void setWorklogDAO(IWorklogDAO worklogDAO) {
        this.worklogDAO = worklogDAO;
    }

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
     * Makes a HTTP post request to the tempo api containing the Jira user keys of the workers and a date range to
     * retrieve the worklogs matching that query.
     *
     * @param worklogRequestDTO Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return List of all worklogs that were retrieved from the client server between the two given dates for the specified workers.
     */
    public List<OriginWorklogDTO> retrieveWorklogsFromClientServer(WorklogRequestDTO worklogRequestDTO) {
        setClientUrl(jiraSynchronisationProperties.getOriginUrl());

        HttpResponse<JsonNode> jsonWorklogs = requestWorklogs(worklogRequestDTO);

        if (jsonWorklogs.getBody() == null || !jsonWorklogs.getBody().isArray()) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = jsonWorklogs.getBody().getArray();

        return createWorklogDTOs(jsonArray);
    }

    /**
     * Creates WorklogDTOs from the passed in JSONArray and puts them in a list
     *
     * @param jsonArray All retrieved worklogs in jsonArray form.
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    private List<OriginWorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
        List<OriginWorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                String worker = jsonObject.getString("worker");
                String started = jsonObject.getString("started");
                String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
                int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");
                int worklogId = jsonObject.getInt("tempoWorklogId");

                worklogs.add(
                        (OriginWorklogDTO) new OriginWorklogDTO()
                                .setWorklogId(worklogId)
                                .setWorker(worker)
                                .setStarted(started)
                                .setOriginTaskId(originTaskId)
                                .setTimeSpentSeconds(timeSpentSeconds)
                );

            } catch (JSONException e) {
                return new ArrayList<>();
            }
        }

        return worklogs;
    }

    /**
     * Sends the actual HTTP post request for the worklogs to the Tempo API
     *
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
     * the location of where the worklog should be created is specified by the originTaskId in the {@link DestinationWorklogDTO}.
     * the standard comment of the {@link DestinationWorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from client Jira-server.
     * @return A map of worklogDTO's with their corresponding status codes after being posted.
     */
    public Map createWorklogsOnAvisiServer(List<DestinationWorklogDTO> worklogs) {
        setAvisiUrl(jiraSynchronisationProperties.getDestinationUrl());

        Map<DestinationWorklogDTO, Integer> responseCodes = new HashMap<>();

        for (DestinationWorklogDTO worklog : worklogs) {
            HttpResponse<JsonNode> response = request.post(avisiUrl, worklog);
            responseCodes.put(worklog, response.getStatus());
        }

        return responseCodes;
    }

    public void synchronise() {
        List<UserSyncDTO> autoSyncUsers = userDAO.getAllAutoSyncUsers();

        List<String> originJiraUserKeys = autoSyncUsers.stream()
                .map(UserSyncDTO::getFromWorker)
                .collect(Collectors.toList());

        WorklogRequestDTO requestBody = new WorklogRequestDTO()
                .setFrom("Laatste sync datum ophalen")
                .setWorker(originJiraUserKeys)
                .setTo("Nu");

        List<DestinationWorklogDTO> filteredOutWorklogs = filterOutAlreadySyncedWorklogs(retrieveWorklogsFromClientServer(requestBody), worklogDAO.getAllWorklogIds());

        List<DestinationWorklogDTO> worklogsToBeSynced = mapDestinationUserKeyToOriginUserKey(filteredOutWorklogs, userDAO);

        createWorklogsOnAvisiServer(worklogsToBeSynced);

        //todo make createworklogsonavisiserver return originDTOs with response codes mapped and add succesfully posted worklogs to the database.

    }

    /**
     * Filters out worklogs from the worklogs retrieved from the origin server by
     * comparing the already synced worklogIds and removing any match from the original
     * list.
     *
     * @param retrievedWorklogs Worklogs that were retrieved from the origin server
     * @param allWorklogIds All worklogIds of worklogs that are already synced in the past.
     *                      This data is retrieved from the database
     * @return list of DestinationWorklogDTOs that only contain not yet synced worklogs
     */
    public List<DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(List<OriginWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
        return retrievedWorklogs
                .stream()
                .filter(worklog -> allWorklogIds.stream()
                        .noneMatch(worklogId -> worklogId == worklog.getWorklogId()))
                .collect(Collectors.toList());
    }

    /**
     * Transforms a OriginWorklogDTO list to a DestinationWorklogDTO List.
     * This is needed because OriginWorklogDTO contains the worklogId that
     * is used to filter out already synced worklogs, but to post the worklog
     * to the destination server it can't contain the worklogId anymore. Hence
     * the transformation of the type of the list. Also simply casting it won't work
     * this is why it is streamed. The filter for nonNull is simply applied
     * because the stream requires some kind of non-terminal operation to work.
     *
     * @param originWorklogDTOs List of originWorklogDTOs
     * @return The same list that was passed in but the type changed to DestinationWorklogDTO
     */
    public List<DestinationWorklogDTO> transformFromOriginToDestination(List<OriginWorklogDTO> originWorklogDTOs) {
         return originWorklogDTOs.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
