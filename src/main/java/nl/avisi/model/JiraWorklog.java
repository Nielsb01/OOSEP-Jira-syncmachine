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
import nl.avisi.model.contracts.IJiraWorklog;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for retrieving and creating worklogs on the specified Jira server through the Tempo API with HTTP requests
 */
public class JiraWorklog implements IJiraWorklog {

    /**
     * base URL where the Jira server of the client is being hosted
     */
    private String originUrl;

    /**
     * base URL where the Jira server of Avisi is being hosted
     */
    private String destinationUrl;

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

    public void setOriginUrl(String originUrl) {
        this.originUrl = String.format("%srest/tempo-timesheets/4/worklogs/search", originUrl);
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = String.format("%srest/tempo-timesheets/4/worklogs", destinationUrl);
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
    public Map<Integer, DestinationWorklogDTO> retrieveWorklogsFromOriginServer(WorklogRequestDTO worklogRequestDTO) {

        setOriginUrl(jiraSynchronisationProperties.getOriginUrl());

        HttpResponse<JsonNode> jsonWorklogs = requestWorklogs(worklogRequestDTO);

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
     * @return List of all worklogs that were retrieved between the two given dates for the specified workers.
     */
    protected Map<Integer, DestinationWorklogDTO> createWorklogDTOs(JSONArray jsonArray) {
        Map<Integer, DestinationWorklogDTO> worklogs = new HashMap<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            try {
                String worker = jsonObject.getString("worker");
                String started = jsonObject.getString("started");
                String originTaskId = jsonObject.getJSONObject("issue").getString("accountKey");
                int timeSpentSeconds = jsonObject.getInt("timeSpentSeconds");
                int worklogId = jsonObject.getInt("tempoWorklogId");

                worklogs.put(worklogId, new DestinationWorklogDTO()
                        .setWorker(worker)
                        .setStarted(started)
                        .setOriginTaskId(originTaskId)
                        .setTimeSpentSeconds(timeSpentSeconds));




            } catch (JSONException e) {
                return new HashMap<>();
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

        return request.post(originUrl, requestBody);
    }

    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link DestinationWorklogDTO}.
     * the standard comment of the {@link DestinationWorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from client Jira-server.
     * @return A map of worklogDTO's with their corresponding status codes after being posted.
     */
    public Map<Integer, Integer> createWorklogsOnDestinationServer(Map<Integer, DestinationWorklogDTO> worklogs) {
        setDestinationUrl(jiraSynchronisationProperties.getDestinationUrl());

        Map<Integer, Integer> responseCodes = new HashMap<>();

        for (Integer worklogId : worklogs.keySet()) {
            HttpResponse<JsonNode> response = request.post(destinationUrl, worklogs.get(worklogId));
            responseCodes.put(worklogId, response.getStatus());
        }

        return responseCodes;
    }


    /**
     * Synchronises the worklogs of one person after the user sent
     * a request to manually synchronise their worklogs.
     * @param worklogRequestDTO Contains the neccesary information to
     *                          make a HTTP request to the Tempo API
     *                          to retrieve worklogs from the
     *                          origin server
     * @param userId Id of the user that wants to manually synchronise their
     *               worklogs
     */
    public void manualSynchronisation(WorklogRequestDTO worklogRequestDTO, int userId) {
        List<DestinationWorklogDTO> allWorklogsFromOriginServer = retrieveWorklogsFromOriginServer(worklogRequestDTO);

         List<UserSyncDTO> userSyncDTO =  new ArrayList<>();
         userSyncDTO.add(userDAO.getSyncUser(userId));

       // List<DestinationWorklogDTO> filteredWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = createWorklogsOnDestinationServer(replaceOriginUserKeyWithCorrectDestinationUserKey(allWorklogsFromOriginServer, userSyncDTO));

        //List<Integer> succesfullyPostedWorklogIds = filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

            //todo functionaliteit inbouwen voor afhandelen van failed posted worklogs en
            //refactor zodat autosync en manualsync allebei gebruik maken van dezelfde
            //sync method

        //succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    /**
     * This method is called once a week by {@link nl.avisi.timer.SynchroniseTask} to
     * synchronise all the worklogs from the origin server to the destination server.
     * Only worklogs from users with auto synchronisation enabled will be synchronised.
     * The ids of successfully posted worklogs will be added to the database to prevent
     * wrongfully synchronising worklogs in the future.
     */
    public void synchronise() {
        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        List<UserSyncDTO> autoSyncUsers = userDAO.getAllAutoSyncUsers();


        List<String> originJiraUserKeys = autoSyncUsers.stream()
                .map(UserSyncDTO::getOriginWorker)
                .collect(Collectors.toList());

        //TODO: Functionaliteit van Max toepassen bij het zetten van de datums
        WorklogRequestDTO requestBody = new WorklogRequestDTO("", "",originJiraUserKeys);


        List<DestinationWorklogDTO> allWorklogsFromOriginServer = retrieveWorklogsFromOriginServer(requestBody);

        //List<DestinationWorklogDTO> filteredOutWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        List<DestinationWorklogDTO> worklogsToBeSynced = replaceOriginUserKeyWithCorrectDestinationUserKey(allWorklogsFromOriginServer, autoSyncUsers);

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = createWorklogsOnDestinationServer(worklogsToBeSynced);

        //List<Integer> succesfullyPostedWorklogIds = filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        /*
        TODO: onsuccesvol gesplaatste worklogs verwerken (met groep overleggen wat er moet gebeuren).
        TODO: Synchronise refactoren zodat autoSync en manualSync deze beide kunnen aanspreken
         */

       // succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    /**
     * Filters all worklogs retrieved from the origin server
     * that match with the worklogs that were posted to the
     * destination server and have a status code 200.
     *
     * @param allRetrievedWorklogsFromOriginServer All the worklogs that were retrieved from the origin server
     * @param postedWorklogsWithResponseCodes Map of worklogs that were posted with the respective response status
     * @return List of all the worklogIds that had a status code of 200
     */
    protected List<Integer> filterOutFailedPostedWorklogs(List<OriginWorklogDTO> allRetrievedWorklogsFromOriginServer, Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes) {
        List<Integer> idsOfSuccesfullyPostedworklogs = new ArrayList<>();

        postedWorklogsWithResponseCodes.forEach((key, value) -> allRetrievedWorklogsFromOriginServer.forEach(worklog -> {
            if (worklog.equals(key) && value == 200) {
                idsOfSuccesfullyPostedworklogs.add(worklog.getWorklogId());
            }
        }));

        return idsOfSuccesfullyPostedworklogs;

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
    protected List<DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(List<OriginWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
        return retrievedWorklogs
                .stream()
                .filter(worklog -> allWorklogIds.stream()
                        .noneMatch(worklogId -> worklogId == worklog.getWorklogId()))
                .collect(Collectors.toList());
    }

    /**
     * Maps the worker field of a destinationWorklogDTO after it has been set with
     * the origin worker user key, to the destination worker key that matches the
     * origin worker user key.
     *
     * @param worklogsToBeSynced List of DestinationWorklogDTO where the worker field contains the origin user key
     *                           which will be swapped for the destination user key
     * @param autoSyncUsers List of all the users that have auto sync enabled
     * @return A list of worklogs with the correct user key mapped to the worker field
     */
    protected List<DestinationWorklogDTO> replaceOriginUserKeyWithCorrectDestinationUserKey(List<DestinationWorklogDTO> worklogsToBeSynced, List<UserSyncDTO> autoSyncUsers) {

        List<DestinationWorklogDTO> worklogsWithoutMatchingKey = new ArrayList<>();

        worklogsToBeSynced.forEach(worklog -> {
            Optional<String> matchingKey = autoSyncUsers.stream()
                    .filter(user -> user.getOriginWorker().equals(worklog.getWorker()))
                    .map(UserSyncDTO::getDestinationWorker)
                    .reduce((u, v) -> {
                        throw new IllegalStateException("More than one user key found");
                    });

            if (matchingKey.isPresent()) {
                worklog.setWorker(matchingKey.get());
            } else {
                worklogsWithoutMatchingKey.add(worklog);
            }
        });

        worklogsWithoutMatchingKey.forEach(worklogsToBeSynced::remove);

        return worklogsToBeSynced;
    }
}
