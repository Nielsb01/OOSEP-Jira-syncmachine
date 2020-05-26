package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.DestinationWorklogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
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
//        // Arrange
//        int expected = 200;
//
//        when(mockedTempoInterface.createWorklogOnDestinationServer(any())).thenReturn(mockedHttpResponse);
//        when(mockedHttpResponse.getStatus()).thenReturn(expected);
//
//        // Act
//        Map<DestinationWorklogDTO, Integer> actual = sut.createWorklogsOnDestinationServer(new ArrayList<>());
//
//        // Assert
//        assertEquals(expected, actual.get());
    }

    @Test
    void testCreateWorklogsOnDestinationServerReturnsThreeResponseCodes() {
        // Arrange
        int expected = 3;

        List<DestinationWorklogDTO> destinationWorklogDTOS = new ArrayList<>();
        destinationWorklogDTOS.add(new DestinationWorklogDTO());
        destinationWorklogDTOS.add(new DestinationWorklogDTO());
        destinationWorklogDTOS.add(new DestinationWorklogDTO());

        when(mockedTempoInterface.createWorklogOnDestinationServer(any())).thenReturn(mockedHttpResponse);
        when(mockedHttpResponse.getStatus()).thenReturn(200);

        // Act
        Map<DestinationWorklogDTO, Integer> actual = sut.createWorklogsOnDestinationServer(destinationWorklogDTOS);

        // Assert
        assertEquals(expected, actual.size());
    }
}
