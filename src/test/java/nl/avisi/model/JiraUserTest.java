package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.api.JiraInterface;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.dto.UserPreferenceDTO;
import nl.avisi.logger.ILogger;
import nl.avisi.model.exceptions.InvalidUsernameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.InternalServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

class JiraUserTest {

    private static final String JIRAUSER_1000 = "JIRAUSER1000";
    private static final String JIRAUSER_1010 = "JIRAUSER1010";
    private static final String KEY = "key";
    private static final String DESTINATION_USERNAME = "AvisiUsername";
    private static final String ORIGIN_USERNAME = "ClientUsername";
    private static final String EMPTY_JSON_ARRAY = "[]";
    private static final int USER_ID = 1;

    private static final int HTTP_STATUS_OK = 200;
    private static final int WRONG_HTTP_STATUS = 404;

    private JiraUser sut;

    private IUserDAO mockedUserDAO;
    private JiraInterface mockedJiraInterface;
    private ILogger mockedLogger;

    private JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO(ORIGIN_USERNAME, DESTINATION_USERNAME);

    @BeforeEach
    void setUp() {
        sut = new JiraUser();

        mockedUserDAO = mock(IUserDAO.class);
        sut.setUserDAO(mockedUserDAO);

        mockedJiraInterface = mock(JiraInterface.class);
        mockedLogger = mock(ILogger.class);
        sut.setJiraInterface(mockedJiraInterface);
        sut.setLogger(mockedLogger);
    }

    @Test
    void testGetAutoSyncPreferenceReturnsGetUserAutoSyncPreference() {
        // Arrange
        UserPreferenceDTO expected = new UserPreferenceDTO(true);
        Mockito.when(mockedUserDAO.getUserAutoSyncPreference(USER_ID)).thenReturn(expected);

        // Act
        UserPreferenceDTO actual = sut.getAutoSyncPreference(USER_ID);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testSetAutoSyncPreferenceCallsSetAutoPreferenceWithCorrectParams() {
        //Arrange
        boolean autoSyncOn = true;

        //Act
        sut.setAutoSyncPreference(USER_ID, autoSyncOn);

        //Assert
        Mockito.verify(mockedUserDAO).setAutoSyncPreference(USER_ID, autoSyncOn);
    }

    @Test
    void testSetJiraUserKeysCallsUpdateJiraUserKeysWithCorrectParams() {
        //Arrange
        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(originJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(destinationJsonString));

        //Act
        sut.setJiraUserKeys(jiraUsernameDTO, USER_ID);

        //Assert
        Mockito.verify(mockedUserDAO).updateJiraUserKeys(any(JiraUserKeyDTO.class), eq(USER_ID));
    }

    @Test
    void testSetJiraUserKeysThrowsInvalidUsernameExceptionWhenJSONExceptionOccurs() {
        // Arrange
        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(EMPTY_JSON_ARRAY));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(destinationJsonString));

        // Assert
        assertThrows(InvalidUsernameException.class, () ->
                //Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenOriginKeyIsEmpty() {
        // Arrange
        JSONObject emptyKeyJsonObject = new JSONObject()
                .put(KEY, "");
        String emptyKeyJsonString = new JSONArray().put(emptyKeyJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(emptyKeyJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(destinationJsonString));

        // Assert
        assertThrows(InvalidUsernameException.class, () ->
                //Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenDestinationKeyIsEmpty() {
        // Arrange
        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, "");
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(originJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(destinationJsonString));

        // Assert
        assertThrows(InvalidUsernameException.class, () ->
                // Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInternalServerErrorWhenJiraReturnsErrorStatusForOriginUserKey() {
        // Arrange
        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(WRONG_HTTP_STATUS);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(originJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(destinationJsonString));

        // Assert
        assertThrows(InternalServerErrorException.class, () ->
                // Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInternalServerErrorWhenJiraReturnsErrorStatusForDestinationUserKey() {
        // Arrange
        JSONObject emptyKeyJsonObject = new JSONObject()
                .put("", JIRAUSER_1000);
        String emptyKeyJsonString = new JSONArray().put(emptyKeyJsonObject).toString();

        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = mock(HttpResponse.class);

        Mockito.when(mockedOriginUserKey.getStatus()).thenReturn(HTTP_STATUS_OK);
        Mockito.when(mockedDestinationUserKey.getStatus()).thenReturn(WRONG_HTTP_STATUS);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(originJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(emptyKeyJsonString));

        // Assert
        assertThrows(InternalServerErrorException.class, () ->
                // Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }
}
