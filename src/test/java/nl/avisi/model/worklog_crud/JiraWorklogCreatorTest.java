package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.DestinationWorklogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class JiraWorklogCreatorTest {

    private JiraWorklogCreator sut;

    private TempoInterface mockedTempoInterface;
    private HttpResponse mockedHttpResponse;

    @BeforeEach
    void setUp() {
        sut = new JiraWorklogCreator();

        mockedTempoInterface = Mockito.mock(TempoInterface.class);
        sut.setTempoInterface(mockedTempoInterface);

        mockedHttpResponse = Mockito.mock(HttpResponse.class);
    }

    @Test
    void testCreateWorklogsOnDestinationServerReturnsTempoInterfaceCreateWorklogOnDestinationServerResponseCode() {
        // Arrange
        int expected = 200;

        when(mockedTempoInterface.createWorklogOnDestinationServer(any())).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getStatus()).thenReturn(expected);

        Map<Integer, DestinationWorklogDTO> destinationWorklogDTOS = new HashMap<Integer, DestinationWorklogDTO>();
        destinationWorklogDTOS.put(1, new DestinationWorklogDTO());

        // Act
        Map<Integer, Integer> actual = sut.createWorklogsOnDestinationServer(destinationWorklogDTOS);
        int actualStatus = actual.get(actual.keySet().toArray()[0]);

        // Assert
        assertEquals(expected, actualStatus);
    }

    @Test
    void testCreateWorklogsOnDestinationServerReturnsThreeResponseCodes() {
        // Arrange
        int expected = 3;

        Map<Integer, DestinationWorklogDTO> destinationWorklogDTOS = new HashMap<Integer, DestinationWorklogDTO>();
        destinationWorklogDTOS.put(1, new DestinationWorklogDTO());
        destinationWorklogDTOS.put(2, new DestinationWorklogDTO());
        destinationWorklogDTOS.put(3, new DestinationWorklogDTO());

        when(mockedTempoInterface.createWorklogOnDestinationServer(any())).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getStatus()).thenReturn(200);

        // Act
        Map<Integer, Integer> actual = sut.createWorklogsOnDestinationServer(destinationWorklogDTOS);

        // Assert
        assertEquals(expected, actual.size());
    }
}
