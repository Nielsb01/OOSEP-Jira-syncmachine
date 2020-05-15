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

    public static final String JIRAUSER_1000 = "JIRAUSER1000";
    public static final String JIRAUSER_1010 = "JIRAUSER1010";
    public static final String TEST_HOTMAIL_COM = "test@hotmail.com";
    public static final String TEST_GMAIL_COM = "test@gmail.com";
    public static final String KEY = "key";
    public static final String DESTINATION_USERNAME = "AvisiUsername";
    public static final String ORIGIN_USERNAME = "ClientUsername";
    public static final String EMPTY_JSON_ARRAY = "[]";
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
                .setDestinationUsername(DESTINATION_USERNAME)
                .setOriginUsername(ORIGIN_USERNAME);
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

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        JiraUserKeyDTO result = sut.retrieveJiraUserKeyByUsername(jiraUsernameDTO);

        //Assert
        assertEquals(JIRAUSER_1010, result.getOriginUserKey());
        assertEquals(JIRAUSER_1000, result.getDestinationUserKey());

    }

    @Test
    void testRetrieveJiraUserKeyByUsernameThrowsInvalidUsernameExceptionWhenJSONExceptionOccurs() {
        //Arrange
        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(EMPTY_JSON_ARRAY));

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
                .setDestinationUsername(TEST_GMAIL_COM)
                .setOriginUsername(TEST_HOTMAIL_COM);

        JSONObject originJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1010);
        String originJsonString = new JSONArray().put(originJsonObject).toString();

        JSONObject destinationJsonObject = new JSONObject()
                .put(KEY, JIRAUSER_1000);
        String destinationJsonString = new JSONArray().put(destinationJsonObject).toString();

        when(mockedRequest.get(any())).thenReturn(mockedResponse);
        when(mockedResponse.getBody()).thenReturn(new JsonNode(originJsonString), new JsonNode(destinationJsonString));

        //Act
        sut.setJiraUserKeys(jiraUsernameDTO, userId);

        //Assert
        verify(mockedUserDAO).updateJiraUserKeys(anyObject(), anyInt());
    }
}
