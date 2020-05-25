package nl.avisi.model;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.contracts.IJiraWorklog;
import nl.avisi.model.mutators.WorklogMutator;
import nl.avisi.model.worklog_crud.WorklogCreator;
import nl.avisi.model.worklog_crud.WorklogRetriever;

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

    private WorklogMutator worklogMutator;

    private WorklogRetriever worklogRetriever;

    private WorklogCreator worklogCreator;

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setWorklogDAO(IWorklogDAO worklogDAO) {
        this.worklogDAO = worklogDAO;
    }

    @Inject
    public void setWorklogMutator(WorklogMutator worklogMutator) {
        this.worklogMutator = worklogMutator;
    }

    @Inject
    public void setWorklogRetriever(WorklogRetriever worklogRetriever) {
        this.worklogRetriever = worklogRetriever;
    }

    @Inject
    public void setWorklogCreator(WorklogCreator worklogCreator) {
        this.worklogCreator = worklogCreator;
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
        List<OriginWorklogDTO> allWorklogsFromOriginServer = worklogRetriever.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        List<UserSyncDTO> userSyncDTO = new ArrayList<>();
        userSyncDTO.add(userDAO.getSyncUser(userId));

        List<DestinationWorklogDTO> filteredWorklogs = worklogMutator.filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = worklogCreator.createWorklogsOnDestinationServer(worklogMutator.replaceOriginUserKeyWithCorrectDestinationUserKey(filteredWorklogs, userSyncDTO));

        List<Integer> succesfullyPostedWorklogIds = worklogMutator.filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

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
        WorklogRequestDTO requestBody = new WorklogRequestDTO("", "",originJiraUserKeys);


        List<OriginWorklogDTO> allWorklogsFromOriginServer = worklogRetriever.retrieveWorklogsFromOriginServer(requestBody);

        List<DestinationWorklogDTO> filteredOutWorklogs = worklogMutator.filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        List<DestinationWorklogDTO> worklogsToBeSynced = worklogMutator.replaceOriginUserKeyWithCorrectDestinationUserKey(filteredOutWorklogs, autoSyncUsers);

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = worklogCreator.createWorklogsOnDestinationServer(worklogsToBeSynced);

        List<Integer> succesfullyPostedWorklogIds = worklogMutator.filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        /*
        TODO: onsuccesvol gesplaatste worklogs verwerken (met groep overleggen wat er moet gebeuren).
        TODO: Synchronise refactoren zodat autoSync en manualSync deze beide kunnen aanspreken
         */

        succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }
}
