package nl.avisi.controller;

import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.model.JiraUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserControllerTest {

    public static final int HTTP_STATUS_OK = 200;
    private UserController sut;
    private JiraUser mockedJiraUser;

    @BeforeEach
    void setUp() {
        sut = new UserController();
        mockedJiraUser = mock(JiraUser.class);
        sut.setJiraUser(mockedJiraUser);
    }

    @Test
    void testSetJiraUserKeysResponseStatus() {
        //Arrange
        final JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO();
        final int userId = 1;

        //Act
        final Response actualValue = sut.setJiraUserKeys(jiraUsernameDTO, userId);

        //Assert
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }

    @Test
    void testSetJiraUserKeysCallsSetJiraUserKeys() {
        final int userId = 1;
        //Arrange
        final JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO();
        final int userId = 1;

        //Act
        final Response actualValue = sut.setJiraUserKeys(jiraUsernameDTO, userId);

        //Assert
        verify(mockedJiraUser).setJiraUserKeys(jiraUsernameDTO, userId);
    }

    @Test
    void testsetAutoSyncPreferenceCallsSetAutoSyncPreference() {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        //Act
        final Response actualValue = sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        verify(mockedJiraUser).setAutoSyncPreference(userId, autoSyncOn);
    }

    @Test
    void testsetAutoSyncPreferenceResponseStatus() {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        //Act
        final Response actualValue = sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }


}
