package nl.avisi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;

class UserControllerTest {

    private UserController sut;
    private JiraUser mockedJiraUser;

    @BeforeEach
    void setUp() {
        sut = new UserController();
        mockedJiraUser = mock(JiraUser.class);
        sut.setJiraUser(mockedJiraUser);
    }

    @Test
    void testSetJiraUserKeys() {
        // Setup
        final JiraUsernameDTO jiraUsernameDTO = new JiraUsernameDTO();

        // Run the test
        final Response result = sut.setJiraUserKeys(jiraUsernameDTO);

        // Verify the results
    }
}
