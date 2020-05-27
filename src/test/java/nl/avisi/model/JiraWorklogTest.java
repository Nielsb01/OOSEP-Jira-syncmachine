package nl.avisi.model;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.ManualSyncDTO;
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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

class JiraWorklogTest {

    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_UNAUTHORIZED = 401;
    private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

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
        sut.manualSynchronisation(manualSyncDTO, 0);

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
        sut.manualSynchronisation(manualSyncDTO, 0);

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
}
