package nl.avisi.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.IRequest;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class TempoInterfaceTest {
    
    TempoInterface sut;
    
    IRequest mockedIRequest;
    JiraSynchronisationProperties mockedJirasynchronisationProperties;

    @BeforeEach
    void setUp() {
        sut = new TempoInterface();
        
        mockedIRequest = Mockito.mock(IRequest.class);
        sut.setRequest(mockedIRequest);
        
        mockedJirasynchronisationProperties = Mockito.mock(JiraSynchronisationProperties.class);
        sut.setJiraSynchronisationProperties(mockedJirasynchronisationProperties);
    }

    @Test
    void testRequestOriginWorklogReturnsPostResult() {
        // Assert
        String originUrl = "url";
        WorklogRequestDTO requestBody = new WorklogRequestDTO();

        HttpResponse<JsonNode> expected = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedJirasynchronisationProperties.getOriginUrl()).thenReturn(originUrl);
        Mockito.when(mockedIRequest.post(originUrl, requestBody)).thenReturn(expected);

        // Act
        HttpResponse<JsonNode> actual = sut.requestOriginJiraWorklogs(requestBody);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testCreateWorklogOnDestinationServerReturnsPostResult() {
        // Assert
        String destinationUrl = "url";
        DestinationWorklogDTO requestBody = new DestinationWorklogDTO();

        HttpResponse<JsonNode> expected = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedJirasynchronisationProperties.getDestinationUrl()).thenReturn(destinationUrl);
        Mockito.when(mockedIRequest.post(destinationUrl, requestBody)).thenReturn(expected);

        // Act
        HttpResponse<JsonNode> actual = sut.createWorklogOnDestinationServer(requestBody);

        // Assert
        assertEquals(expected, actual);
    }
}
