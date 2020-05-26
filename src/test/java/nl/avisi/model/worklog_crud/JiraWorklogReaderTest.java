package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class JiraWorklogReaderTest {

    private JiraWorklogReader sut;

    private TempoInterface mockedTempoInterface;
    private HttpResponse mockedHttpResponse;

    private static final String WORKER_VALUE = "ttt";
    private static final String STARTED_VALUE = "fff";
    private static final String ACCOUNT_KEY_VALUE = "kkk";
    private static final int TIME_SPENT_SECONDS_VALUE = 1234;
    private static final int TEMPO_WORKLOG_ID_VALUE = 1;

    private static final JSONObject JSON_OBJECT = new JSONObject()
            .put("worker", WORKER_VALUE)
            .put("started", STARTED_VALUE)
            .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
            .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
            .put("tempoWorklogId", TEMPO_WORKLOG_ID_VALUE);

    private WorklogRequestDTO worklogRequestDTO;

    @BeforeEach
    void setUp() {
        sut = new JiraWorklogReader();

        mockedTempoInterface = Mockito.mock(TempoInterface.class);
        sut.setTempoInterface(mockedTempoInterface);

        mockedHttpResponse = Mockito.mock(HttpResponse.class);

        worklogRequestDTO = new WorklogRequestDTO();
    }

    @Test
    void testReadWorklogsFromOriginServerMapsValuesToCorrectVariablesOfObjectInMap() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
                .put("tempoWorklogId", TEMPO_WORKLOG_ID_VALUE);

        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        Map<Integer, DestinationWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        //Assert
        Object actualMapKey = actual.keySet().toArray()[0];

        assertEquals(WORKER_VALUE, actual.get(actualMapKey).getWorker());
        assertEquals(STARTED_VALUE, actual.get(actualMapKey).getStarted());
        assertEquals(ACCOUNT_KEY_VALUE, actual.get(actualMapKey).getOriginTaskId());
        assertEquals(TIME_SPENT_SECONDS_VALUE, actual.get(actualMapKey).getTimeSpentSeconds());
        assertEquals(TEMPO_WORKLOG_ID_VALUE, actualMapKey);
    }

    @Test
    void testReadWorklogsFromOriginServerCreatesMapWithThreeObjects() {
        //Arrange
        int expectedSize = 3;

        JSONObject jsonObject2 = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
                .put("tempoWorklogId", 2);

        JSONObject jsonObject3 = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE)
                .put("tempoWorklogId", 50);

        String jsonString = new JSONArray()
                .put(JSON_OBJECT)
                .put(jsonObject2)
                .put(jsonObject3)
                .toString();

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        Map<Integer, DestinationWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        //Assert
        assertEquals(expectedSize, actual.size());
    }

    @Test
    void testReadWorklogsFromOriginServerCreatesEmptyMapIfResponseIsNull() {
        // Arrange
        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(null);

        // Act
        Map<Integer, DestinationWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        // Assert
        assertEquals(0, actual.size());
    }

    @Test
    void testReadWorklogsFromOriginServerCreatesEmptyMapIfResponseIsEmpty() {
        // Arrange
        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode("[]"));

        // Act
        Map<Integer, DestinationWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        // Assert
        assertEquals(0, actual.size());
    }

    @Test
    void testReadWorklogsFromOriginServerCreatesEmptyMapIfJsonExceptionOccurs() {
        // Arrange
        int expectedSize = 0;

        JSONObject wrong_json_object = new JSONObject()
                .put("worker", WORKER_VALUE)
                .put("started", STARTED_VALUE)
                .put("issue", new JSONObject().put("accountKey", ACCOUNT_KEY_VALUE))
                .put("timeSpentSeconds", TIME_SPENT_SECONDS_VALUE);

        String jsonString = new JSONArray()
                .put(wrong_json_object)
                .toString();

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode(jsonString));

        // Act
        Map<Integer, DestinationWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        // Assert
        assertEquals(expectedSize, actual.size());
    }
}
