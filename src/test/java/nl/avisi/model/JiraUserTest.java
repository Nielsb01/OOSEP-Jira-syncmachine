package nl.avisi.model;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import nl.avisi.api.JiraInterface;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.model.exceptions.InvalidUsernameException;
import nl.avisi.network.IRequest;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.*;

class JiraUserTest {

    public static final String JIRAUSER_1000 = "JIRAUSER1000";
    public static final String JIRAUSER_1010 = "JIRAUSER1010";
    public static final String TEST_HOTMAIL_COM = "test@hotmail.com";
    public static final String TEST_GMAIL_COM = "test@gmail.com";
    public static final String KEY = "key";
    public static final String DESTINATION_USERNAME = "AvisiUsername";
    public static final String ORIGIN_USERNAME = "ClientUsername";
    public static final String EMPTY_JSON_ARRAY = "[]";
    private JiraUser sut;

    private IUserDAO mockedUserDAO;
    private JiraInterface mockedJiraInterface;

    private HttpResponse mockedResponse;
    private JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO(ORIGIN_USERNAME, DESTINATION_USERNAME);

    @BeforeEach
    void setUp() {
        sut = new JiraUser();

        mockedResponse = Mockito.mock(HttpResponse.class);

        mockedUserDAO = Mockito.mock(IUserDAO.class);
        sut.setUserDAO(mockedUserDAO);
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameReturnsCorrectJiraUserKeys() {
        //Arrange
        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        Mockito.when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        JiraUserKeyDTO result = sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO);

        //Assert
        assertEquals(JIRAUSER_1010, result.getOriginUserKey());
        assertEquals(JIRAUSER_1000, result.getDestinationUserKey());
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenJSONExceptionOccurs() {
        //Arrange
        Mockito.when(mockedResponse.getBody()).thenReturn(new JsonNode(EMPTY_JSON_ARRAY));

        //Assert
        assertThrows(InvalidUsernameException.class, () ->
                //Act
                sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO));
    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenKeyIsEmpty() {
        //Arrange
        JSONObject jsonObject = new JSONObject()
                .put(KEY, "");
        String jsonString = new JSONArray().put(jsonObject).toString();

        Mockito.when(mockedResponse.getBody()).thenReturn(new JsonNode(jsonString));

        //Assert
        assertThrows(InvalidUsernameException.class, () ->

                //Act
                sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO));
    }

    @Test
    void testSetAutoSyncPreferenceCallsSetAutoPreferenceWithCorrectParams() {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        //Act
        sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        Mockito.verify(mockedUserDAO).setAutoSyncPreference(userId, autoSyncOn);
    }

    @Test
    void testSetJiraUserKeysCallsUpdateJiraUserKeys() {
        //Arrange
        final int userId = 1;
        JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO(TEST_HOTMAIL_COM, TEST_GMAIL_COM);

        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        Mockito.when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        sut.setJiraUserKeys(jiraUsernameDTO, userId);

        //Assert
        Mockito.verify(mockedUserDAO).updateJiraUserKeys(anyObject(), anyInt());
    }
}
