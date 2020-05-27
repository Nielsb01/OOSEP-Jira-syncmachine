package nl.avisi.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.network.IRequest;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class JiraInterfaceTest {

    private JiraInterface sut;

    private IRequest mockedIRequest;
    private JiraSynchronisationProperties mockedJiraSynchronisationProperties;

    @BeforeEach
    void setUp() {
        sut = new JiraInterface();

        mockedIRequest = Mockito.mock(IRequest.class);
        sut.setRequest(mockedIRequest);

        mockedJiraSynchronisationProperties = Mockito.mock(JiraSynchronisationProperties.class);
        sut.setJiraSynchronisationProperties(mockedJiraSynchronisationProperties);
    }

    @Test
    void testGetOriginUserKeySendsRightGetRequest() {
        // Arrange
        final String apiUrl = "rest/api/2/user/search?username=";
        String originJiraUrl = "originUrl";
        String username = "username";

        String jiraRetrieveUserKeyUrl = originJiraUrl + apiUrl + username;

        HttpResponse expected = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedJiraSynchronisationProperties.getOriginUrl()).thenReturn(originJiraUrl);
        Mockito.when(mockedIRequest.get(jiraRetrieveUserKeyUrl)).thenReturn(expected);

        // Act
        HttpResponse<JsonNode> actual = sut.getOriginUserKey(username);

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    void testGetDestinationUserKeySendsRightGetRequest() {
        // Arrange
        final String apiUrl = "rest/api/2/user/search?username=";
        String destinationJiraUrl = "destinationUrl";
        String username = "username";

        String jiraRetrieveUserKeyUrl = destinationJiraUrl + apiUrl + username;

        HttpResponse expected = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedJiraSynchronisationProperties.getDestinationUrl()).thenReturn(destinationJiraUrl);
        Mockito.when(mockedIRequest.get(jiraRetrieveUserKeyUrl)).thenReturn(expected);

        // Act
        HttpResponse<JsonNode> actual = sut.getDestinationUserKey(username);

        // Assert
        assertEquals(expected, actual);
    }
}
