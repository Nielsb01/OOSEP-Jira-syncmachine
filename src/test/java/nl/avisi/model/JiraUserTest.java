package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.InvalidUsernameException;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.network.IRequest;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
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
    private JiraUsernameDTO jiraUsernameDTO;

    @BeforeEach
    void setUp() {
        sut = new JiraUser();
        mockedProperties = mock(JiraSynchronisationProperties.class);
        mockedRequest = mock(IRequest.class);
        mockedResponse = mock(HttpResponse.class);

        sut.setJiraSynchronisationProperties(mockedProperties);
        sut.setRequest(mockedRequest);

        jiraUsernameDTO = new JiraUsernameDTO()
                .setAvisiUsername("AvisiUsername")
                .setClientUsername("ClientUsername");
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameReturnsCorrectJiraUserKeys() {
        //Arrange
        JSONObject avisiJsonObject = new JSONObject()
                .put("key", "JIRAUSER1000");
        String avisiJsonString = new JSONArray().put(avisiJsonObject).toString();

        JSONObject clientJsonObject = new JSONObject()
                .put("key", "JIRAUSER1010");
        String clientJsonString = new JSONArray().put(clientJsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(avisiJsonString), new JsonNode(clientJsonString));

        //Act
        final JiraUserKeyDTO result = sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO);

        //Assert
        assertEquals("JIRAUSER1000", result.getAvisiUserKey());
        assertEquals("JIRAUSER1010", result.getClientUserKey());

    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenJSONExceptionOccurs() {
        //Arrange
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode("[]"));

        //Assert
        assertThrows(InvalidUsernameException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO));
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
                sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO));
    }

}
