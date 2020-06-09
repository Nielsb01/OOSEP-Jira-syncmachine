package nl.avisi.model;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.ManualSyncDTO;
import nl.avisi.dto.SynchronisedDataDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.model.worklog_crud.JiraWorklogCreator;
import nl.avisi.model.worklog_crud.JiraWorklogReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

class JiraWorklogTest {

    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_UNAUTHORIZED = 401;
    private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
    private static final int USER_ID = 1;

    private JiraWorklog sut;
    private IUserDAO mockedUserDao;
    private IWorklogDAO mockedWorklogDao;
    private JiraWorklogReader mockedWorklogReader;
    private JiraWorklogCreator mockedWorklogCreator;

    private final String originWorker = "USER10000";
    private final String destinationWorker = "USER10100";

    private final String syncFromDate = "30-12-2020";
    private final String syncUntilDate = "31-12-2020";

    private final int timeSpentSeconds = 3600;
    private final String originTaskId = "task_1";

    private final Map<Integer, DestinationWorklogDTO> destinationWorklogs = new HashMap<>();

    @BeforeEach
    void setUp() {
        sut = new JiraWorklog();

        mockedUserDao = mock(IUserDAO.class);
        mockedWorklogDao = mock(IWorklogDAO.class);
        mockedWorklogReader = mock(JiraWorklogReader.class);
        mockedWorklogCreator = mock(JiraWorklogCreator.class);

        sut.setWorklogDAO(mockedWorklogDao);
        sut.setJiraWorklogCreator(mockedWorklogCreator);
        sut.setUserDAO(mockedUserDao);
        sut.setJiraWorklogReader(mockedWorklogReader);

        destinationWorklogs.put(1, new DestinationWorklogDTO(originWorker, "started", timeSpentSeconds, originTaskId));
        destinationWorklogs.put(2, new DestinationWorklogDTO(originWorker, "started", timeSpentSeconds, originTaskId));
        destinationWorklogs.put(3, new DestinationWorklogDTO(originWorker, "started", timeSpentSeconds, originTaskId));
    }

    @Test
    void testManualSynchronisationSavesSuccessfullySyncedWorklogs() {
        // Arrange
        final UserSyncDTO syncUser = new UserSyncDTO(originWorker, destinationWorker);
        final ManualSyncDTO manualSyncDTO = new ManualSyncDTO(syncFromDate, syncUntilDate);

        when(mockedUserDao.getSyncUser(anyInt())).thenReturn(syncUser);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_OK);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act
        sut.manualSynchronisation(manualSyncDTO, USER_ID);

        // Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size())).addWorklogId(anyInt());
    }

    @Test
    void testManualSynchronisationDoesntSaveUnsuccessfulSyncedWorklogs() {
        // Arrange
        final UserSyncDTO syncUser = new UserSyncDTO(originWorker, destinationWorker);
        final ManualSyncDTO manualSyncDTO = new ManualSyncDTO(syncFromDate, syncUntilDate);

        when(mockedUserDao.getSyncUser(anyInt())).thenReturn(syncUser);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_UNAUTHORIZED);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_UNAUTHORIZED);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act
        sut.manualSynchronisation(manualSyncDTO, USER_ID);

        // Assert
        verify(mockedWorklogDao, never()).addWorklogId(anyInt());
    }

    @Test
    void testAutoSynchronisationSavesSuccessfullySyncedWorklogs() {
        // Arrange
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));

        when(mockedUserDao.getAllAutoSyncUsers()).thenReturn(syncUsers);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_OK);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act
        sut.autoSynchronisation(syncFromDate, syncUntilDate);

        // Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size())).addWorklogId(anyInt());
    }

    @Test
    void testAutoSynchronisationDoesntSaveUnsuccessfulSyncedWorklogs() {
        // Arrange
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));

        when(mockedUserDao.getAllAutoSyncUsers()).thenReturn(syncUsers);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_UNAUTHORIZED);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_UNAUTHORIZED);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act
        sut.autoSynchronisation(syncFromDate, syncUntilDate);

        // Assert
        verify(mockedWorklogDao, never()).addWorklogId(anyInt());
    }

    @Test
    void testAutoSynchronisationThrowsIllegalStateExceptionWhenMultipleDestinationUserKeysAreFound() {
        // Arrange
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));

        when(mockedUserDao.getAllAutoSyncUsers()).thenReturn(syncUsers);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_UNAUTHORIZED);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_UNAUTHORIZED);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act & Assert
        Assertions.assertThrows(IllegalStateException.class, () -> {
            sut.autoSynchronisation(syncFromDate, syncUntilDate);
        });
    }

    @Test
    void testAutomaticSynchronisationIgnoresAlreadySyncedWorklogs() {
        // Arrange
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));

        when(mockedUserDao.getAllAutoSyncUsers()).thenReturn(syncUsers);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final List<Integer> alreadySyncedWorklogs = new ArrayList<>();
        alreadySyncedWorklogs.add(1);
        alreadySyncedWorklogs.add(2);

        when(mockedWorklogDao.getAllWorklogIds()).thenReturn(alreadySyncedWorklogs);

        final Map<Integer, Integer> successfullySyncedWorklogs = new HashMap<>();
        successfullySyncedWorklogs.put(3, HTTP_STATUS_OK);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(successfullySyncedWorklogs);

        // Act
        sut.autoSynchronisation(syncFromDate, syncUntilDate);

        // Assert
        final int expectedNumberOfSyncedWorklogs = destinationWorklogs.size() - alreadySyncedWorklogs.size();
        verify(mockedWorklogDao, times(expectedNumberOfSyncedWorklogs)).addWorklogId(anyInt());
    }

    @Test
    void testAutoSynchronisationRemovesWorklogWithoutMatchingDestinationKey() {
        // Arrange
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));
        syncUsers.add(new UserSyncDTO(originWorker, destinationWorker));

        Map<Integer, DestinationWorklogDTO> destinationWorklogDTOMap = new HashMap<>();
        destinationWorklogDTOMap.put(1, new DestinationWorklogDTO("JIRAUSER50", "started", timeSpentSeconds, originTaskId));

        Map<Integer, DestinationWorklogDTO> emptyWorklogMap = new HashMap<>();

        when(mockedUserDao.getAllAutoSyncUsers()).thenReturn(syncUsers);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogDTOMap);

        //Act
        sut.autoSynchronisation(syncFromDate, syncUntilDate);

        // Assert
        verify(mockedWorklogCreator, times(1)).createWorklogsOnDestinationServer(emptyWorklogMap);
    }

    @Test
    void testManualSynchronisationReturnsCorrectSynchronisationData() {
        // Arrange
        int totalSynchronisedWorklogs = 2;
        int totalFailedSynchronisedWorklogs = 1;
        int totalFailedSynchronisedSeconds = 3600;
        int totalSynchronisedSeconds = 7200;

        ManualSyncDTO manualSyncDTO = new ManualSyncDTO(syncFromDate, syncUntilDate);
        final List<UserSyncDTO> syncUsers = new ArrayList<>();
        UserSyncDTO userSyncDTO = new UserSyncDTO(originWorker, destinationWorker);

        when(mockedUserDao.getSyncUser(USER_ID)).thenReturn(userSyncDTO);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_OK);

        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        // Act
        SynchronisedDataDTO result = sut.manualSynchronisation(manualSyncDTO, USER_ID);

        // Assert
        assertEquals(result.getTotalSynchronisedSeconds(), totalSynchronisedSeconds);
        assertEquals(result.getTotalFailedSynchronisedSeconds(), totalFailedSynchronisedSeconds);
        assertEquals(result.getTotalFailedSynchronisedWorklogs(), totalFailedSynchronisedWorklogs);
        assertEquals(result.getTotalSynchronisedWorklogs(), totalSynchronisedWorklogs);
    }

    @Test
    void testManualSynchronisationSavesfailedWorklogs() {
        // Arrange
        final UserSyncDTO syncUser = new UserSyncDTO(originWorker, destinationWorker);
        final ManualSyncDTO manualSyncDTO = new ManualSyncDTO(syncFromDate, syncUntilDate);

        when(mockedUserDao.getSyncUser(anyInt())).thenReturn(syncUser);
        when(mockedWorklogReader.retrieveWorklogsFromOriginServer(anyObject())).thenReturn(destinationWorklogs);

        // Act
        sut.manualSynchronisation(manualSyncDTO, 0);

        // Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size())).addFailedworklog(anyInt(), anyObject());
    }

    @Test
    void testSynchroniseFailedWorklogsCallsGetAllFailedWorklogs() {
        //Arrange
        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_INTERNAL_SERVER_ERROR);

        when(mockedWorklogDao.getAllFailedWorklogs()).thenReturn(destinationWorklogs);
        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        //Act
        sut.synchroniseFailedWorklogs();

        //Assert
        verify(mockedWorklogDao).getAllFailedWorklogs();
    }

    @Test
    void testSynchroniseFailedWorklogsCallsCreateWorklogs() {
        //Arrange
        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_INTERNAL_SERVER_ERROR);

        when(mockedWorklogDao.getAllFailedWorklogs()).thenReturn(destinationWorklogs);
        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        //Act
        sut.synchroniseFailedWorklogs();

        //Assert
        verify(mockedWorklogCreator).createWorklogsOnDestinationServer(destinationWorklogs);
    }

    @Test
    void testSynchroniseFailedWorklogsCallsAddFailedWorklogCorrectAmountOfTimes() {
        //Arrange
        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_INTERNAL_SERVER_ERROR);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_OK);

        when(mockedWorklogDao.getAllFailedWorklogs()).thenReturn(destinationWorklogs);
        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        //Act
        sut.synchroniseFailedWorklogs();

        //Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size() - 1)).addFailedworklog(anyInt(), anyObject());
    }

    @Test
    void testSynchroniseFailedWorklogsCallsAddWorklogIdCorrectAmountOfTimes() {
        //Arrange
        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_INTERNAL_SERVER_ERROR);

        when(mockedWorklogDao.getAllFailedWorklogs()).thenReturn(destinationWorklogs);
        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        //Act
        sut.synchroniseFailedWorklogs();

        //Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size() - 1)).addWorklogId(anyInt());
    }

    @Test
    void testSynchroniseFailedWorklogsCallsDeleteFailedWorklogCorrectAmountOfTimes() {
        //Arrange
        final Map<Integer, Integer> worklogsWithResponseCodes = new HashMap<>();
        worklogsWithResponseCodes.put(1, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(2, HTTP_STATUS_OK);
        worklogsWithResponseCodes.put(3, HTTP_STATUS_INTERNAL_SERVER_ERROR);

        when(mockedWorklogDao.getAllFailedWorklogs()).thenReturn(destinationWorklogs);
        when(mockedWorklogCreator.createWorklogsOnDestinationServer(anyMap())).thenReturn(worklogsWithResponseCodes);

        //Act
        sut.synchroniseFailedWorklogs();

        //Assert
        verify(mockedWorklogDao, times(destinationWorklogs.size() - 1)).deleteFailedWorklog(anyInt());
    }
}
