package nl.avisi.model;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import nl.avisi.api.TempoInterface;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for retrieving and creating worklogs on the specified Jira server through the Tempo API with HTTP requests
 */
public class JiraWorklog {

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

    private WorklogMutator worklogMutator;

    @Inject
    public void setWorklogMutator(WorklogMutator worklogMutator) {
        this.worklogMutator = worklogMutator;
    }

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

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    private TempoInterface tempoInterface;

    @Inject
    public void setTempoInterface(TempoInterface tempoInterface) {
        this.tempoInterface = tempoInterface;
    }


    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link DestinationWorklogDTO}.
     * the standard comment of the {@link DestinationWorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from client Jira-server.
     * @return A map of worklogDTO's with their corresponding status codes after being posted.
     */
    public Map<DestinationWorklogDTO, Integer> createWorklogsOnDestinationServer(List<DestinationWorklogDTO> worklogs) {
        Map<DestinationWorklogDTO, Integer> responseCodes = new HashMap<>();

        for (DestinationWorklogDTO worklog : worklogs) {
            HttpResponse<JsonNode> response = tempoInterface.createWorklogOnDestinationServer(worklog);
            responseCodes.put(worklog, response.getStatus());
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
        List<OriginWorklogDTO> allWorklogsFromOriginServer = retrieveWorklogsFromOriginServer(worklogRequestDTO);

        List<UserSyncDTO> userSyncDTO = new ArrayList<>();
        userSyncDTO.add(userDAO.getSyncUser(userId));

        List<DestinationWorklogDTO> filteredWorklogs = worklogMutator.filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = createWorklogsOnDestinationServer(worklogMutator.replaceOriginUserKeyWithCorrectDestinationUserKey(filteredWorklogs, userSyncDTO));

        List<Integer> succesfullyPostedWorklogIds = worklogMutator.filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

            //todo functionaliteit inbouwen voor afhandelen van failed posted worklogs en
            //refactor zodat autosync en manualsync allebei gebruik maken van dezelfde
            //sync method

        succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    /**
     * Makes a HTTP post request to the tempo api containing the Jira user keys of the workers and a date range to
     * retrieve the worklogs matching that query.
     *
     * @param worklogRequestDTO Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return List of all worklogs that were retrieved from the client server between the two given dates for the specified workers.
     */
    public List<OriginWorklogDTO> retrieveWorklogsFromOriginServer(WorklogRequestDTO worklogRequestDTO) {
        HttpResponse<JsonNode> jsonWorklogs = tempoInterface.requestOriginJiraWorklogs(worklogRequestDTO);

        if (jsonWorklogs.getBody() == null || !jsonWorklogs.getBody().isArray()) {
            return new ArrayList<>();
        }

        JSONArray worklogJsonArray = jsonWorklogs.getBody().getArray();

        return worklogMutator.createWorklogDTOs(worklogJsonArray);
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


        List<OriginWorklogDTO> allWorklogsFromOriginServer = retrieveWorklogsFromOriginServer(requestBody);

        List<DestinationWorklogDTO> filteredOutWorklogs = worklogMutator.filterOutAlreadySyncedWorklogs(allWorklogsFromOriginServer, worklogDAO.getAllWorklogIds());

        List<DestinationWorklogDTO> worklogsToBeSynced = worklogMutator.replaceOriginUserKeyWithCorrectDestinationUserKey(filteredOutWorklogs, autoSyncUsers);

        Map<DestinationWorklogDTO, Integer> postedWorklogsWithResponseCodes = createWorklogsOnDestinationServer(worklogsToBeSynced);

        List<Integer> succesfullyPostedWorklogIds = worklogMutator.filterOutFailedPostedWorklogs(allWorklogsFromOriginServer, postedWorklogsWithResponseCodes);

        /*
        TODO: onsuccesvol gesplaatste worklogs verwerken (met groep overleggen wat er moet gebeuren).
        TODO: Synchronise refactoren zodat autoSync en manualSync deze beide kunnen aanspreken
         */

        succesfullyPostedWorklogIds.forEach(worklogId -> worklogDAO.addWorklogId(worklogId));
    }

    }
