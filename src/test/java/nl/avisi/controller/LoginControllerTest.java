package nl.avisi.controller;

import nl.avisi.dto.LoginDTO;
import nl.avisi.model.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    public static final int HTTP_STATUS_CREATED = 201;
    private LoginController sut;
    private Login mockedLogin;

    @BeforeEach
    void setUp() {
        sut = new LoginController();
        mockedLogin = mock(Login.class);
        sut.setLogin(mockedLogin);
    }

    @Test
    void testLoginResponseEntity() {
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        when(mockedLogin.validateCredentials(loginDTO)).thenReturn("Succes");
        // Run the test
        final Response actualValue = sut.login(loginDTO);

        // Verify the results
        assertEquals("Succes", actualValue.getEntity());
    }

    @Test
    void testLoginResponseStatus() {
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        // Run the test
        final Response actualValue = sut.login(loginDTO);

        // Verify the results
        assertEquals(HTTP_STATUS_CREATED, actualValue.getStatus());
    }
}
