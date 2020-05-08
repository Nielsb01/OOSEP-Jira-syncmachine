package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.dto.WorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.JiraWorklog;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class JiraWorklogTest {

    private JiraWorklog sut;
    private IRequest mockedRequest;
    private HttpResponse mockedResponse;

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

        sut.setClientUrl("http://127.0.0.1/");
        sut.setAvisiUrl("http://127.0.0.1/");
        sut.setRequest(mockedRequest);
        sut.setBasicAuth(new BasicAuth());

        worklogRequestDTO = new WorklogRequestDTO();
    }

    @Test
    void testRetrieveAllWorklogsCreatesListWithOneObject() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE);

        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<WorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        //Assert
        assertEquals(1, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsNull() {

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(null);

        List<WorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListWhenResponseIsEmpty() {

        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(
                "[]"));

        List<WorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsMapsValuesToCorrectVariablesOfObjectInList() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE);

        String jsonString = new JSONArray().put(jsonObject).toString();


        when(mockedRequest.post(any(), any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<WorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);

        //Assert
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
        List<WorklogDTO> actualValue = sut.retrieveWorklogsFromClientServer(worklogRequestDTO);


        //Assert
        assertEquals(0, actualValue.size());

    }

    @Test
    public void testWhileAddingWorklogsCheckMapIsSameLengthAsWorklogs() {
        // Arrange
        List<WorklogDTO> mockWorklogs= new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-2"));

        when(mockedRequest.post(any(),any())).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(200);

        // Act
        Map actualvalue = sut.createWorklogsOnAvisiServer(mockWorklogs);

        //Assert
        assertEquals(2,actualvalue.size());
    }

    @Test
    public void testWhileAddingWorklogsCheckMapNotAllStatuscodes200() {
        // Arrange
        List<WorklogDTO> mockWorklogs= new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-4"));

        when(mockedRequest.post(any(),any())).thenReturn(mockedResponse);
        when(mockedResponse.getStatus()).thenReturn(200,400);

        // Act
        Map actualvalue = sut.createWorklogsOnAvisiServer(mockWorklogs);

        //Assert
        assertTrue(actualvalue.containsValue(400));
        assertTrue(actualvalue.containsValue(200));
    }
}
