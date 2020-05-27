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
import nl.avisi.model.exceptions.InvalidUsernameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.*;

class JiraUserTest {

    public static final String JIRAUSER_1000 = "JIRAUSER1000";
    public static final String JIRAUSER_1010 = "JIRAUSER1010";
    public static final String KEY = "key";
    public static final String DESTINATION_USERNAME = "AvisiUsername";
    public static final String ORIGIN_USERNAME = "ClientUsername";
    public static final String EMPTY_JSON_ARRAY = "[]";
    private static final int USER_ID = 1;

    private JiraUser sut;

    private IUserDAO mockedUserDAO;
    private JiraInterface mockedJiraInterface;

    private JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO(ORIGIN_USERNAME, DESTINATION_USERNAME);

    @BeforeEach
    void setUp() {
        sut = new JiraUser();

        mockedUserDAO = Mockito.mock(IUserDAO.class);
        sut.setUserDAO(mockedUserDAO);

        mockedJiraInterface = Mockito.mock(JiraInterface.class);
        sut.setJiraInterface(mockedJiraInterface);
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

        HttpResponse<JsonNode> mockedOriginUserKey = Mockito.mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = Mockito.mock(HttpResponse.class);

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

        HttpResponse<JsonNode> mockedOriginUserKey = Mockito.mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = Mockito.mock(HttpResponse.class);

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
                .put("", JIRAUSER_1000);
        String emptyKeyJsonString = new JSONArray().put(emptyKeyJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = Mockito.mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = Mockito.mock(HttpResponse.class);

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
        JSONObject emptyKeyJsonObject = new JSONObject()
                .put("", JIRAUSER_1000);
        String emptyKeyJsonString = new JSONArray().put(emptyKeyJsonObject).toString();

        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        HttpResponse<JsonNode> mockedOriginUserKey = Mockito.mock(HttpResponse.class);
        HttpResponse<JsonNode> mockedDestinationUserKey = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedJiraInterface.getOriginUserKey(ORIGIN_USERNAME)).thenReturn(mockedOriginUserKey);
        Mockito.when(mockedJiraInterface.getDestinationUserKey(DESTINATION_USERNAME)).thenReturn(mockedDestinationUserKey);

        Mockito.when(mockedOriginUserKey.getBody()).thenReturn(new JsonNode(originJsonString));
        Mockito.when(mockedDestinationUserKey.getBody()).thenReturn(new JsonNode(emptyKeyJsonString));

        // Assert
        assertThrows(InvalidUsernameException.class, () ->
                // Act
                sut.setJiraUserKeys(jiraUsernameDTO, USER_ID));
    }
}
