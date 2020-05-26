package nl.avisi.model;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.contracts.IJiraWorklog;
import nl.avisi.model.worklog_crud.JiraWorklogCreator;
import nl.avisi.model.worklog_crud.JiraWorklogReader;
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

    private IUserDAO userDAO;

    private IWorklogDAO worklogDAO;

    private JiraWorklogReader jiraWorklogReader;

    private JiraWorklogCreator jiraWorklogCreator;

    @Inject
    public void setWorklogDAO(IWorklogDAO worklogDAO) {
        this.worklogDAO = worklogDAO;
    }

    @Inject
    public void setJiraWorklogCreator(JiraWorklogCreator jiraWorklogCreator) {
        this.jiraWorklogCreator = jiraWorklogCreator;
    }

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setJiraWorklogReader(JiraWorklogReader jiraWorklogReader) {
        this.jiraWorklogReader = jiraWorklogReader;
    }


    /**
     * Synchronises the worklogs of one person after the user sent
     * a request to manually synchronise their worklogs.
     *
     * @param worklogRequestDTO Contains the neccesary information to
     *                          make a HTTP request to the Tempo API
     *                          to retrieve worklogs from the
     *                          origin server
     * @param userId Id of the user that wants to manually synchronise their
     *               worklogs
     */
    @Override
    public void manualSynchronisation(WorklogRequestDTO worklogRequestDTO, int userId) {
        Map<Integer, DestinationWorklogDTO> allWorklogsFromOriginServer = jiraWorklogReader.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        List<UserSyncDTO> userSyncDTO = new ArrayList<>();
        userSyncDTO.add(userDAO.getSyncUser(userId));

        // List<DestinationWorklogDTO> filteredWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<Integer, Integer> postedWorklogsWithResponseCodes = jiraWorklogCreator.createWorklogsOnDestinationServer(replaceOriginUserKeyWithCorrectDestinationUserKey(allWorklogsFromOriginServer, userSyncDTO));

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
    @Override
    public void synchronise() {
        Date date = new Date();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        List<UserSyncDTO> autoSyncUsers = userDAO.getAllAutoSyncUsers();


        List<String> originJiraUserKeys = autoSyncUsers.stream()
                .map(UserSyncDTO::getOriginWorker)
                .collect(Collectors.toList());

        //TODO: Functionaliteit van Max toepassen bij het zetten van de datums
        WorklogRequestDTO requestBody = new WorklogRequestDTO("", "", originJiraUserKeys);


        Map<Integer, DestinationWorklogDTO> allWorklogsFromOriginServer = jiraWorklogReader.retrieveWorklogsFromOriginServer(requestBody);

        //List<DestinationWorklogDTO> filteredOutWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<Integer, DestinationWorklogDTO> worklogsToBeSynced = replaceOriginUserKeyWithCorrectDestinationUserKey(allWorklogsFromOriginServer, autoSyncUsers);

        Map<Integer, Integer> postedWorklogsWithResponseCodes = jiraWorklogCreator.createWorklogsOnDestinationServer(worklogsToBeSynced);

        //List<Integer> succesfullyPostedWorklogIds = filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        /*
        TODO: onsuccesvol gesplaatste worklogs verwerken (met groep overleggen wat er moet gebeuren).
        TODO: Synchronise refactoren zodat autoSync en manualSync deze beide kunnen aanspreken
         */

        // succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    /**
     * Filters all worklogsIds retrieved from the origin server
     * that have a status code 200.
     *
     * @param postedWorklogsWithResponseCodes Map of worklogsIds with their status codes
     * @return List of all the worklogIds that have a status code of 200
     */
    protected List<Integer> filterOutFailedPostedWorklogs(Map<Integer, Integer> postedWorklogsWithResponseCodes) {
        return postedWorklogsWithResponseCodes
                .entrySet().stream()
                .filter(worklog -> worklog.getValue() != 200)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Filters out worklogs from the worklogs retrieved from the origin server by
     * comparing the already synced worklogIds and removing any match from the original
     * list.
     *
     * @param retrievedWorklogs Worklogs that were retrieved from the origin server
     * @param allWorklogIds All worklogIds of worklogs that are already synced in the past.
     *                     This data is retrieved from the database
     * @return Map of DestinationWorklogDTOs that only contain not yet synced worklogs
     */
    protected Map<Integer, DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(Map<Integer, DestinationWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
        return retrievedWorklogs.entrySet()
                .stream()
                .filter(worklog -> allWorklogIds.stream()
                        .noneMatch(worklogId -> worklogId.equals(worklog.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Maps the worker field of a destinationWorklogDTO after it has been set with
     * the origin worker user key, to the destination worker key that matches the
     * origin worker user key.
     *
     * @param worklogsToBeSynced Map of worklogIds with DestinationWorklogDTO where the worker field contains the origin user key
     *                           which will be swapped for the destination user key
     * @param autoSyncUsers List of all the users that needed to be synchronised
     * @return A map of worklogIds with worklogs that have the correct user key mapped to the worker field
     */
    protected Map<Integer, DestinationWorklogDTO> replaceOriginUserKeyWithCorrectDestinationUserKey(Map<Integer, DestinationWorklogDTO> worklogsToBeSynced, List<UserSyncDTO> autoSyncUsers) {

        List<Integer> worklogsWithoutMatchingKey = new ArrayList<>();

        for (Integer worklogId : worklogsToBeSynced.keySet()) {
            Optional<String> matchingKey = autoSyncUsers.stream()
                    .filter(user -> user.getOriginWorker().equals(worklogsToBeSynced.get(worklogId).getWorker()))
                    .map(UserSyncDTO::getDestinationWorker)
                    .reduce((u, v) -> {
                        throw new IllegalStateException("More than one user key found");
                    });

            if (matchingKey.isPresent()) {
                worklogsToBeSynced.get(worklogId).setWorker(matchingKey.get());
            } else {
                worklogsWithoutMatchingKey.add(worklogId);
            }
        }

        worklogsWithoutMatchingKey.forEach(worklogsToBeSynced::remove);

        return worklogsToBeSynced;
    }
}
