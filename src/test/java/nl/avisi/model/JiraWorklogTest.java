package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.UserSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class JiraWorklogTest {

    public static final int TEMPO_WORKLOG_ID_VALUE = 1;
    private JiraWorklog sut;
    private IRequest mockedRequest;
    private HttpResponse mockedResponse;
    private JiraSynchronisationProperties mockedProperties;

    private final String WORKER_VALUE = "ttt";
    private final String STARTED_VALUE = "fff";
    private final String ACCOUNT_KEY_VALUE = "kkk";
    private final int TIME_SPENT_SECONDS_VALUE = 1234;

    private WorklogRequestDTO worklogRequestDTO;

    @BeforeEach
    void setUp() {

        sut = new JiraWorklog();
        mockedRequest = mock(IRequest.class);
        mockedResponse = mock(HttpResponse.class);
        mockedProperties = mock(JiraSynchronisationProperties.class);

        sut.setClientUrl("http://127.0.0.1/");
        sut.setAvisiUrl("http://127.0.0.1/");
        sut.setRequest(mockedRequest);
        sut.setBasicAuth(new BasicAuth());
        sut.setJiraSynchronisationProperties(mockedProperties);

        worklogRequestDTO = new WorklogRequestDTO();
    }

    @Test
    void testRetrieveAllWorklogsCreatesListWithOneObject() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
                .put("tempoWorklogId", TEMPO_WORKLOG_ID_VALUE);

        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        //Assert
        assertEquals(1, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsNull() {


        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(null);

        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListWhenResponseIsEmpty() {

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(
                "[]"));

        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsMapsValuesToCorrectVariablesOfObjectInList() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
                .put("tempoWorklogId", TEMPO_WORKLOG_ID_VALUE);

        String jsonString = new JSONArray().put(jsonObject).toString();


        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        //Assert
        assertEquals(TEMPO_WORKLOG_ID_VALUE, actualValue.get(0).getWorklogId());
        assertEquals(WORKER_VALUE, actualValue.get(0).getWorker());
        assertEquals(STARTED_VALUE, actualValue.get(0).getStarted());
        assertEquals(ACCOUNT_KEY_VALUE, actualValue.get(0).getOriginTaskId());
        assertEquals(TIME_SPENT_SECONDS_VALUE, actualValue.get(0).getTimeSpentSeconds());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListWhenAccountKeyIsMissing() {
        //Arrange
        JSONObject jsonString = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE);

        String jsonArray = new JSONArray().put(jsonString).toString();

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonArray));

        //Act
        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);


        //Assert
        assertEquals(0, actualValue.size());

    }

    @Test
    public void testWhileAddingWorklogsCheckMapIsSameLengthAsWorklogs() {
        // Arrange
        List<DestinationWorklogDTO> mockWorklogs = new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new DestinationWorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new DestinationWorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-2"));

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(200);

        // Act
        Map actualvalue = sut.createWorklogsOnAvisiServer(mockWorklogs);

        //Assert
        assertEquals(2, actualvalue.size());
    }

    @Test
    public void testWhileAddingWorklogsCheckMapNotAllStatuscodes200() {
        // Arrange
        List<DestinationWorklogDTO> mockWorklogs = new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new DestinationWorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new DestinationWorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-4"));

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(200, 400);

        // Act
        Map actualvalue = sut.createWorklogsOnAvisiServer(mockWorklogs);

        //Assert
        assertTrue(actualvalue.containsValue(400));
        assertTrue(actualvalue.containsValue(200));
    }

    @Test
    void testTransformFromOriginToDestinationReturnsListWithSameObjectAsBefore() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        OriginWorklogDTO originWorklogDTO = new OriginWorklogDTO().setWorklogId(1);
        originList.add(originWorklogDTO);

        //Act
        List<DestinationWorklogDTO> destinationList = sut.transformFromOriginToDestination(originList);

        //Assert
        assertEquals(originWorklogDTO, destinationList.get(0));
    }

    @Test
    void testTransformFromOriginToDestinationFiltersOutNullObjects() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        originList.add(null);

        //Act
        List<DestinationWorklogDTO> destinationList = sut.transformFromOriginToDestination(originList);

        //Assert
        assertEquals(0, destinationList.size());
    }

    @Test
    void testFilterOutAlreadySyncedWorklogsReturnsCorrectlySizedListAfterFiltering() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        originList.add(new OriginWorklogDTO().setWorklogId(1));
        originList.add(new OriginWorklogDTO().setWorklogId(2));
        originList.add(new OriginWorklogDTO().setWorklogId(3));

        List<Integer> allWorklogIds = new ArrayList<>();
        allWorklogIds.add(1);
        allWorklogIds.add(2);

        //Act
        List<DestinationWorklogDTO> destinationList = sut.filterOutAlreadySyncedWorklogs(originList, allWorklogIds);

        //Assert
        assertEquals(1, destinationList.size());
    }

    @Test
    void testFilterOutAlreadySyncedWorklogsReturnsListWithCorrectObject() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        OriginWorklogDTO originWorklogDTO = new OriginWorklogDTO().setWorklogId(3);
        originList.add(new OriginWorklogDTO().setWorklogId(1));
        originList.add(new OriginWorklogDTO().setWorklogId(2));
        originList.add(originWorklogDTO);

        List<Integer> allWorklogIds = new ArrayList<>();
        allWorklogIds.add(1);
        allWorklogIds.add(2);

        //Act
        List<DestinationWorklogDTO> destinationList = sut.filterOutAlreadySyncedWorklogs(originList, allWorklogIds);

        //Assert
        assertEquals(originWorklogDTO, destinationList.get(0));
    }

    @Test
    void testmapDestinationUserKeyToOriginUserKeyReturnsObjectsWithCorrectlyMappedUserKeys() {
        //Arrange
        List<DestinationWorklogDTO> destinationWorklogDTOS = new ArrayList<>();
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER10"));
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER11"));

        List<UserSyncDTO> userSyncDTOS = new ArrayList<>();
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER10").setToWorker("JIRAUSER20"));
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER11").setToWorker("JIRAUSER21"));

        //Act
        List<DestinationWorklogDTO> destinationList = sut.mapDestinationUserKeyToOriginUserKey(destinationWorklogDTOS, userSyncDTOS);

        //Assert
        assertEquals("JIRAUSER20", destinationList.get(0).getWorker());
        assertEquals("JIRAUSER21", destinationList.get(1).getWorker());
    }

    @Test
    void testmapDestinationUserKeyToOriginUserKeyThrowsIllegalStateExceptionWhenMultipleMatchingOriginUserKeysAreFound() {
        //Arrange
        List<DestinationWorklogDTO> destinationWorklogDTOS = new ArrayList<>();
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER10"));
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER11"));

        List<UserSyncDTO> userSyncDTOS = new ArrayList<>();
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER10").setToWorker("JIRAUSER20"));
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER10").setToWorker("JIRAUSER21"));

        //Act & Assert
        assertThrows(IllegalStateException.class, () -> sut.mapDestinationUserKeyToOriginUserKey(destinationWorklogDTOS, userSyncDTOS));
    }

    @Test
    void testmapDestinationUserKeyToOriginUserKeyRemovesObjectFromListWhenNoMatchingKeyIsFound() {
        //Arrange
        List<DestinationWorklogDTO> destinationWorklogDTOS = new ArrayList<>();
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER10"));
        destinationWorklogDTOS.add(new DestinationWorklogDTO().setWorker("JIRAUSER11"));

        List<UserSyncDTO> userSyncDTOS = new ArrayList<>();
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER9").setToWorker("JIRAUSER20"));
        userSyncDTOS.add(new UserSyncDTO().setFromWorker("JIRAUSER11").setToWorker("JIRAUSER21"));

        //Act
        List<DestinationWorklogDTO> destinationList = sut.mapDestinationUserKeyToOriginUserKey(destinationWorklogDTOS, userSyncDTOS);

        //Assert
        assertEquals(1, destinationList.size());

    }

    @Test
    void testfilterOutFailedPostedWorklogsReturnListWithCorrectObject() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        OriginWorklogDTO originWorklogDTO = new OriginWorklogDTO().setWorklogId(3);
        originList.add(new OriginWorklogDTO().setWorklogId(1));
        originList.add(new OriginWorklogDTO().setWorklogId(2));
        originList.add(originWorklogDTO);

        Map<DestinationWorklogDTO, Integer> worklogsWithResponsecodes = new HashMap<>();
        worklogsWithResponsecodes.put(originWorklogDTO, 200);

        //Act
        List<Integer> actualValue = sut.filterOutFailedPostedWorklogs(originList, worklogsWithResponsecodes);

        //Assert
        assertEquals(originWorklogDTO.getWorklogId(), actualValue.get(0).intValue());

    }

    @Test
    void testfilterOutFailedPostedWorklogsReturnCorrectlySizedList() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        OriginWorklogDTO originWorklogDTO = new OriginWorklogDTO().setWorklogId(3);
        originList.add(new OriginWorklogDTO().setWorklogId(1));
        originList.add(new OriginWorklogDTO().setWorklogId(2));
        originList.add(originWorklogDTO);

        Map<DestinationWorklogDTO, Integer> worklogsWithResponsecodes = new HashMap<>();
        worklogsWithResponsecodes.put(originWorklogDTO, 200);

        //Act
        List<Integer> actualValue = sut.filterOutFailedPostedWorklogs(originList, worklogsWithResponsecodes);

        //Assert
        assertEquals(1, actualValue.size());
    }

    @Test
    void testfilterOutFailedPostedWorklogsReturnEmptyListWhenNoObjectsMatch() {
        //Arrange
        List<OriginWorklogDTO> originList = new ArrayList<>();
        OriginWorklogDTO originWorklogDTO = new OriginWorklogDTO().setWorklogId(3);
        originList.add(new OriginWorklogDTO().setWorklogId(1));
        originList.add(new OriginWorklogDTO().setWorklogId(2));
        originList.add(originWorklogDTO);

        Map<DestinationWorklogDTO, Integer> worklogsWithResponsecodes = new HashMap<>();
        worklogsWithResponsecodes.put(originWorklogDTO, 400);

        //Act
        List<Integer> actualValue = sut.filterOutFailedPostedWorklogs(originList, worklogsWithResponsecodes);

        //Assert
        assertEquals(0, actualValue.size());
    }
}
