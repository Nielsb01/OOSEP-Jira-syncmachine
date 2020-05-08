package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.InvalidEmailException;
import nl.avisi.network.IRequest;
import nl.avisi.propertyReaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JiraUserTest {

    private JiraUser sut;
    private JiraSynchronisationProperties mockedProperties;
    private IRequest mockedRequest;
    private HttpResponse mockedResponse;

    @BeforeEach
    void setUp() {
        sut = new JiraUser();
        mockedProperties = mock(JiraSynchronisationProperties.class);
        mockedRequest = mock(IRequest.class);
        mockedResponse = mock(HttpResponse.class);

        sut.setJiraSynchronisationProperties(mockedProperties);
        sut.setRequest(mockedRequest);
    }

    @Test
    void testRetrieveJiraUserKeyByEmailReturnsCorrectJiraUserKey() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("key", "JIRAUSER1000");
        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedProperties.getAdminPassword()).thenReturn("");
        when(mockedProperties.getAdminUsername()).thenReturn("");
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        final String result = sut.retrieveJiraUserKeyByEmail("email", "server");

        //Assert
        assertEquals("JIRAUSER1000", result);
    }

    @Test
    void testRetrieveJiraUserKeyByEmailThrowsInvalidEmailExceptionWhenJSONExceptionOccurs() {
        //Arrange
        when(mockedProperties.getAdminPassword()).thenReturn("");
        when(mockedProperties.getAdminUsername()).thenReturn("");
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode("[]"));

        //Assert
        assertThrows(InvalidEmailException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByEmail("email", "server"));
    }

    @Test
    void testRetrieveJiraUserKeyByEmailThrowsInvalidEmailExceptionWhenKeyIsEmpty() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("key", "");
        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedProperties.getAdminPassword()).thenReturn("");
        when(mockedProperties.getAdminUsername()).thenReturn("");
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Assert
        assertThrows(InvalidEmailException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByEmail("email", "server"));
    }
}
