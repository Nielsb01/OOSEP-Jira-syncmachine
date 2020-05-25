package nl.avisi.model;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.contracts.IJiraWorklog;
import nl.avisi.model.worklog_crud.JiraWorklogCreator;
import nl.avisi.model.worklog_crud.JiraWorklogRetriever;

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

    private JiraWorklogRetriever jiraWorklogRetriever;

    private JiraWorklogCreator jiraWorklogCreator;

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setWorklogDAO(IWorklogDAO worklogDAO) {
        this.worklogDAO = worklogDAO;
    }

    @Inject
    public void setJiraWorklogRetriever(JiraWorklogRetriever jiraWorklogRetriever) {
        this.jiraWorklogRetriever = jiraWorklogRetriever;
    }

    @Inject
    public void setJiraWorklogCreator(JiraWorklogCreator jiraWorklogCreator) {
        this.jiraWorklogCreator = jiraWorklogCreator;
    }

    /**
     * Synchronises the worklogs of one person after the user sent
     * a request to manually synchronise their worklogs.
     *
     * @param worklogRequestDTO Contains the neccesary information to
     *                          make a HTTP request to the Tempo API
     *                          to retrieve worklogs from the
     *                          origin server
     * @param userId            Id of the user that wants to manually synchronise their
     *                          worklogs
     */
    public void manualSynchronisation(WorklogRequestDTO worklogRequestDTO, int userId) {
        List<OriginWorklogDTO> allWorklogsFromOriginServer = jiraWorklogRetriever.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        List<UserSyncDTO> userSyncDTO = new ArrayList<>();
        userSyncDTO.add(userDAO.getSyncUser(userId));

        List<DestinationWorklogDTO> filteredWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = jiraWorklogCreator.createWorklogsOnDestinationServer(replaceOriginUserKeyWithCorrectDestinationUserKey(filteredWorklogs, userSyncDTO));

        List<Integer> succesfullyPostedWorklogIds = filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        //todo functionaliteit inbouwen voor afhandelen van failed posted worklogs en
        //refactor zodat autosync en manualsync allebei gebruik maken van dezelfde
        //sync method

        succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
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
        WorklogRequestDTO requestBody = new WorklogRequestDTO("", "", originJiraUserKeys);


        List<OriginWorklogDTO> allWorklogsFromOriginServer = jiraWorklogRetriever.retrieveWorklogsFromOriginServer(requestBody);

        List<DestinationWorklogDTO> filteredOutWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        List<DestinationWorklogDTO> worklogsToBeSynced = replaceOriginUserKeyWithCorrectDestinationUserKey(filteredOutWorklogs, autoSyncUsers);

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = jiraWorklogCreator.createWorklogsOnDestinationServer(worklogsToBeSynced);

        List<Integer> succesfullyPostedWorklogIds = filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        /*
        TODO: onsuccesvol gesplaatste worklogs verwerken (met groep overleggen wat er moet gebeuren).
        TODO: Synchronise refactoren zodat autoSync en manualSync deze beide kunnen aanspreken
         */

        succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    /**
     * Filters out worklogs from the worklogs retrieved from the origin server by
     * comparing the already synced worklogIds and removing any match from the original
     * list.
     *
     * @param retrievedWorklogs Worklogs that were retrieved from the origin server
     * @param allWorklogIds     All worklogIds of worklogs that are already synced in the past.
     *                          This data is retrieved from the database
     * @return list of DestinationWorklogDTOs that only contain not yet synced worklogs
     */
    private List<DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(List<OriginWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
        return retrievedWorklogs
                .stream()
                .filter(worklog -> allWorklogIds.stream()
                        .noneMatch(worklogId -> worklogId == worklog.getWorklogId()))
                .collect(Collectors.toList());
    }

    /**
     * Filters all worklogs retrieved from the origin server
     * that match with the worklogs that were posted to the
     * destination server and have a status code 200.
     *
     * @param allRetrievedWorklogsFromOriginServer All the worklogs that were retrieved from the origin server
     * @param postedWorklogsWithResponseCodes      Map of worklogs that were posted with the respective response status
     * @return List of all the worklogIds that had a status code of 200
     */
    private List<Integer> filterOutFailedPostedWorklogs(List<OriginWorklogDTO> allRetrievedWorklogsFromOriginServer, Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes) {
        List<Integer> idsOfSuccesfullyPostedworklogs = new ArrayList<>();

        postedWorklogsWithResponseCodes.forEach((key, value) -> allRetrievedWorklogsFromOriginServer.forEach(worklog -> {
            if (worklog.equals(key) && value == 200) {
                idsOfSuccesfullyPostedworklogs.add(worklog.getWorklogId());
            }
        }));

        return idsOfSuccesfullyPostedworklogs;
    }

    /**
     * Maps the worker field of a destinationWorklogDTO after it has been set with
     * the origin worker user key, to the destination worker key that matches the
     * origin worker user key.
     *
     * @param worklogsToBeSynced List of DestinationWorklogDTO where the worker field contains the origin user key
     *                           which will be swapped for the destination user key
     * @param autoSyncUsers      List of all the users that have auto sync enabled
     * @return A list of worklogs with the correct user key mapped to the worker field
     */
    private List<DestinationWorklogDTO> replaceOriginUserKeyWithCorrectDestinationUserKey(List<DestinationWorklogDTO> worklogsToBeSynced, List<UserSyncDTO> autoSyncUsers) {

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
