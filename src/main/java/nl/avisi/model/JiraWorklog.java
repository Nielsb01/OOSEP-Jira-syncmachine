package nl.avisi.model;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.*;
import nl.avisi.model.contracts.IJiraWorklog;
import nl.avisi.model.worklog_crud.JiraWorklogCreator;
import nl.avisi.model.worklog_crud.JiraWorklogReader;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Responsible for retrieving, creating and synchronising worklogs
 * on the specified Jira server through the Tempo API with HTTP requests
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
     * @param manualSyncDTO Contains the neccesary information to
     *                      make a HTTP request to the Tempo API
     *                      to retrieve worklogs from the
     *                      origin server
     * @param userId        Id of the user that wants to manually synchronise their
     * @return {@link SynchronisedDataDTO} Containing successfully and non successfully
     * synchronised time and worklogs.
     */
    @Override
    public SynchronisedDataDTO manualSynchronisation(ManualSyncDTO manualSyncDTO, int userId) {
        List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(userDAO.getSyncUser(userId));
        List<String> originWorkers = syncUsers.stream().map(UserSyncDTO::getOriginWorker).collect(Collectors.toList());

        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO(
                manualSyncDTO.getFromDate(),
                manualSyncDTO.getUntilDate(),
                originWorkers);

        return synchronise(worklogRequestDTO, syncUsers);
    }

    /**
     * This method is called once a week by {@link nl.avisi.timer.AutomaticSynchronisationTimer} to
     * synchronise all the worklogs from the origin server to the destination server.
     * Only worklogs from users with auto synchronisation enabled will be synchronised.
     * The ids of successfully posted worklogs will be added to the database to prevent
     * wrongfully synchronising worklogs in the future
     *
     * @param fromDate  Date from which to retrieve worklogs. This is the last date
     *                  that auto synchronisation was completed
     * @param untilDate Outer date to retrieve worklog up until this data. This is
     *                  the current date
     */
    @Override
    public void autoSynchronisation(String fromDate, String untilDate) {
        List<UserSyncDTO> syncUsers = userDAO.getAllAutoSyncUsers();

        List<String> originWorkers = syncUsers
                .stream()
                .map(UserSyncDTO::getOriginWorker)
                .collect(Collectors.toList());

        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO(
                fromDate,
                untilDate,
                originWorkers);

        synchronise(worklogRequestDTO, syncUsers);
    }

    @Override
    public void synchroniseFailedWorklogs() {
        Map<Integer, DestinationWorklogDTO> failedWorklogs = worklogDAO.getAllFailedWorklogs();

        List<Integer> successfullyPostedWorklogIds = filterOutFailedPostedWorklogs(jiraWorklogCreator.createWorklogsOnDestinationServer(failedWorklogs));

        getUnsuccessfullyPostedWorklogs(failedWorklogs, successfullyPostedWorklogIds).forEach((worklogId, worklog) -> worklogDAO.addFailedworklog(worklogId, worklog));

        successfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
        successfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.deleteFailedWorklog(worklogId));
    }

    private SynchronisedDataDTO synchronise(WorklogRequestDTO worklogRequestDTO, List<UserSyncDTO> syncUsers) {
        Map<Integer, DestinationWorklogDTO> allWorklogsFromOriginServer = jiraWorklogReader.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        Map<Integer, DestinationWorklogDTO> filteredOutWorklogs = filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<Integer, DestinationWorklogDTO> worklogsToBeSynced = replaceOriginUserKeyWithCorrectDestinationUserKey(filteredOutWorklogs, syncUsers);

        Map<Integer, Integer> postedWorklogsWithResponseCodes = jiraWorklogCreator.createWorklogsOnDestinationServer(worklogsToBeSynced);

        List<Integer> successfullyPostedWorklogIds = filterOutFailedPostedWorklogs(postedWorklogsWithResponseCodes);

        getUnsuccessfullyPostedWorklogs(worklogsToBeSynced, successfullyPostedWorklogIds).forEach((worklogId, worklog) -> worklogDAO.addFailedworklog(worklogId, worklog));

        successfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
        successfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.deleteFailedWorklog(worklogId));

        return calculateSynchronisedData(worklogsToBeSynced, successfullyPostedWorklogIds);
    }

    private Map<Integer, DestinationWorklogDTO> getUnsuccessfullyPostedWorklogs(Map<Integer, DestinationWorklogDTO> worklogsToBeSynced, List<Integer> successfullyPostedWorklogIds) {
        return worklogsToBeSynced
                .entrySet()
                .stream()
                .filter(i -> !successfullyPostedWorklogIds.contains(i.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private SynchronisedDataDTO calculateSynchronisedData(Map<Integer, DestinationWorklogDTO> worklogstoBeSynced, List<Integer> successfullyPostedWorklogIds) {
        int totalTimeToBeSynced = worklogstoBeSynced
                .values()
                .stream()
                .mapToInt(DestinationWorklogDTO::getTimeSpentSeconds)
                .sum();

        int totalSynchronisedSeconds = worklogstoBeSynced
                .entrySet()
                .stream()
                .filter(i -> successfullyPostedWorklogIds.contains(i.getKey()))
                .mapToInt(i -> i.getValue().getTimeSpentSeconds())
                .sum();

        int totalFailedSynchronisedSeconds = totalTimeToBeSynced - totalSynchronisedSeconds;
        int totalWorklogsToBeSynced = worklogstoBeSynced.size();
        int totalSynchronisedWorklogs = successfullyPostedWorklogIds.size();
        int totalFailedSynchronisedWorklogs = totalWorklogsToBeSynced - totalSynchronisedWorklogs;

        return new SynchronisedDataDTO(totalSynchronisedSeconds, totalFailedSynchronisedSeconds, totalSynchronisedWorklogs, totalFailedSynchronisedWorklogs);
    }

    /**
     * Filters all worklogsIds retrieved from the origin server
     * that have a status code 200.
     *
     * @param postedWorklogsWithResponseCodes Map of worklogsIds with their status codes
     * @return List of all the worklogIds that have a status code of 200
     */
    private List<Integer> filterOutFailedPostedWorklogs(Map<Integer, Integer> postedWorklogsWithResponseCodes) {
        return postedWorklogsWithResponseCodes
                .entrySet()
                .stream()
                .filter(worklog -> worklog.getValue() == 200)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Filters out worklogs from the worklogs retrieved from the origin server by
     * comparing the already synced worklogIds and removing any match from the original
     * list.
     *
     * @param retrievedWorklogs Worklogs that were retrieved from the origin server
     * @param allWorklogIds     All worklogIds of worklogs that are already synced in the past.
     *                          This data is retrieved from the database
     * @return Map of DestinationWorklogDTOs that only contain not yet synced worklogs
     */
    private Map<Integer, DestinationWorklogDTO> filterOutAlreadySyncedWorklogs(Map<Integer, DestinationWorklogDTO> retrievedWorklogs, List<Integer> allWorklogIds) {
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
     * @param autoSyncUsers      List of all the users that needed to be synchronised
     * @return A map of worklogIds with worklogs that have the correct user key mapped to the worker field
     */
    private Map<Integer, DestinationWorklogDTO> replaceOriginUserKeyWithCorrectDestinationUserKey(Map<Integer, DestinationWorklogDTO> worklogsToBeSynced, List<UserSyncDTO> autoSyncUsers) {

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
