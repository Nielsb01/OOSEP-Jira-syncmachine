package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveDataTest {

    private RetrieveData sut;
    private IRequest mockedRequest;

    @BeforeEach
    void setUp() {

        sut = new RetrieveData();
        mockedRequest = mock(IRequest.class);
        sut.setUrl("http://127.0.0.1/");
        sut.setRequest(mockedRequest);
        sut.setBasicAuth(new BasicAuth());
    }

    @Test
    void testRetrieveAllWorklogsCreatesListWithOneObject() {
        HttpResponse response = mock(HttpResponse.class);

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(
                "[{\"worker\": \"ttt\",\"started\": \"fff\",\"issue\": {\"accountKey\": \"kk\"},\"timeSpentSeconds\": 1234}]"));

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        assertEquals(1, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListIfResponseIsNull() {
        HttpResponse response = mock(HttpResponse.class);

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(null);

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsCreatesEmptyListWhenResponseIsEmpty() {
        HttpResponse response = mock(HttpResponse.class);

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(
                "[]"));

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        assertEquals(0, actualValue.size());
    }

    @Test
    void testRetrieveAllWorklogsMapsValuesToCorrectVariablesOfObjectInList() {

        String workerValue = "ttt";
        String startedValue = "fff";
        String accountKeyValue = "kkk";
        int timeSpentSecondsValue = 1234;

        String json = String.format("[{\"worker\": \"%s\",\"started\": \"%s\",\"issue\": {\"accountKey\": \"%s\"},\"timeSpentSeconds\": %d}]", workerValue, startedValue, accountKeyValue, timeSpentSecondsValue);

        HttpResponse response = mock(HttpResponse.class);

        when(mockedRequest.post(any(), any())).thenReturn(response);
        when(response.getBody()).thenReturn(new JsonNode(
                json));

        List<WorklogDTO> actualValue = sut.retrieveWorklogs("-", "-", new ArrayList<>());

        assertEquals(workerValue, actualValue.get(0).getWorker());
        assertEquals(startedValue, actualValue.get(0).getStarted());
        assertEquals(accountKeyValue, actualValue.get(0).getOriginTaskId());
        assertEquals(timeSpentSecondsValue, actualValue.get(0).getTimeSpentSeconds());
    }
}
