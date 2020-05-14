package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.exceptions.InvalidUsernameException;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.network.IRequest;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class JiraUserTest {

    private JiraUser sut;
    private JiraSynchronisationProperties mockedProperties;
    private IUserDAO mockedUserDAO;

    private IRequest mockedRequest;
    private HttpResponse mockedResponse;
    private JiraUsernameDTO jiraUsernameDTO;

    @BeforeEach
    void setUp() {
        sut = new JiraUser();

        mockedProperties = mock(JiraSynchronisationProperties.class);
        sut.setJiraSynchronisationProperties(mockedProperties);

        mockedRequest = mock(IRequest.class);
        sut.setRequest(mockedRequest);

        mockedResponse = mock(HttpResponse.class);

        mockedUserDAO = mock(IUserDAO.class);
        sut.setUserDAO(mockedUserDAO);


        jiraUsernameDTO = new JiraUsernameDTO()
                .setDestinationUsername("AvisiUsername")
                .setOriginUsername("ClientUsername");
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameReturnsCorrectJiraUserKeys() {
        //Arrange
        JSONObject originJsonObject = new JSONObject()
                .put("key", "JIRAUSER1010");
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put("key", "JIRAUSER1000");
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        JiraUserKeyDTO result = sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO);

        //Assert
        assertEquals("JIRAUSER1010", result.getOriginUserKey());
        assertEquals("JIRAUSER1000", result.getDestinationUserKey());

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

    @Test
    void testSetJiraUserKeysCallsUpdateJiraUserKeys() {
        //Arrange
        final int userId = 1;
        JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO()
                .setDestinationUsername("test@gmail.com")
                .setOriginUsername("test@hotmail.com");

        JSONObject originJsonObject = new JSONObject()
                .put("key", "JIRAUSER1010");
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put("key", "JIRAUSER1000");
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        sut.setJiraUserKeys(jiraUsernameDTO, userId);

        //Assert
        verify(mockedUserDAO).updateJiraUserKeys(anyObject(), anyInt());
    }
}
