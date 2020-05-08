package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.dto.WorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.RetrieveData;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class RetrieveDataTest {

    private RetrieveData sut;
    private IRequest mockedRequest;
    private HttpResponse response;

    private final String WORKER_VALUE = "ttt";
    private final String STARTED_VALUE = "fff";
    private final String ACCOUNT_KEY_VALUE = "kkk";
    private final int TIME_SPENT_SECONDS_VALUE = 1234;

    @BeforeEach
    void setUp() {

        sut = new RetrieveData();
        mockedRequest = mock(IRequest.class);
        response = mock(HttpResponse.class);

        sut.setUrl("http://127.0.0.1/");
        sut.setRequest(mockedRequest);
        sut.setBasicAuth(new BasicAuth());
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

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        //Assert
        assertEquals(1, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsNull() {

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(null);

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListWhenResponseIsEmpty() {

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(
                "[]"));

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

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


        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

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

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(jsonArray));

        //Act
        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());


        //Assert
        assertEquals(0, actualValue.size());

    }
}
