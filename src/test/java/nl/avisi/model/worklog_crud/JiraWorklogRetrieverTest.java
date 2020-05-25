package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class JiraWorklogRetrieverTest {

    private JiraWorklogRetriever sut;

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
        sut = new JiraWorklogRetriever();

        mockedTempoInterface = Mockito.mock(TempoInterface.class);
        sut.setTempoInterface(mockedTempoInterface);

        mockedHttpResponse = Mockito.mock(HttpResponse.class);

        worklogRequestDTO = new WorklogRequestDTO();
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

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<OriginWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        //Assert
        assertEquals(TEMPO_WORKLOG_ID_VALUE, actual.get(0).getWorklogId());
        assertEquals(WORKER_VALUE, actual.get(0).getWorker());
        assertEquals(STARTED_VALUE, actual.get(0).getStarted());
        assertEquals(ACCOUNT_KEY_VALUE, actual.get(0).getOriginTaskId());
        assertEquals(TIME_SPENT_SECONDS_VALUE, actual.get(0).getTimeSpentSeconds());
    }

    @Test
    void testRetrieveWorklogsFromOriginServerCreatesListWithThreeObjects() {
        //Arrange
        int expectedSize = 3;

        String jsonString = new JSONArray().put(JSON_OBJECT).put(JSON_OBJECT).put(JSON_OBJECT).toString();

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        List<OriginWorklogDTO> actual = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        //Assert
        assertEquals(expectedSize, actual.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsNull() {

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(null);

        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsEmpty() {

        when(mockedTempoInterface.requestOriginJiraWorklogs(worklogRequestDTO)).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getBody()).thenReturn(new JsonNode("[]"));

        List<OriginWorklogDTO> actualValue = sut.retrieveWorklogsFromOriginServer(worklogRequestDTO);

        assertEquals(0, actualValue.size());
    }
}
