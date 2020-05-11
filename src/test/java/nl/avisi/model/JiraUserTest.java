package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.InvalidUsernameException;
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
    void testRetrieveJiraUserKeyByUsernameReturnsCorrectJiraUserKey() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("key", "JIRAUSER1000");
        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Act
        final String result = sut.retrieveJiraUserKeyByUsername("email", "server");

        //Assert
        assertEquals("JIRAUSER1000", result);
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenJSONExceptionOccurs() {
        //Arrange
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode("[]"));

        //Assert
        assertThrows(InvalidUsernameException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByUsername("email", "server"));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenKeyIsEmpty() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put("key", "");
        String jsonString = new JSONArray().put(jsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Assert
        assertThrows(InvalidUsernameException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByUsername("email", "server"));
    }
}