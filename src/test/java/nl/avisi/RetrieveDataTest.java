package nl.avisi;

import kong.unirest.JsonNode;
import nl.avisi.network.IRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveDataTest {

    private RetrieveData retrieveDataUnderTest;
    private IRequest mockedRequest;

    @BeforeEach
    void setUp() {
        retrieveDataUnderTest = new RetrieveData();
        mockedRequest = mock(IRequest.class);
        retrieveDataUnderTest.setRequest(mockedRequest);
    }

    @Test
    void testRetrieveAllWorklogs() {
        JsonNode jsonNode = new JsonNode("");
        when(mockedRequest.post(any(), any())).thenReturn(jsonNode);
    }
}
