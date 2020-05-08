package nl.avisi.controller;

import nl.avisi.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

class LoginControllerTest {

    public static final int HTTP_STATUS_CREATED = 201;
    private LoginController loginControllerUnderTest;

    @BeforeEach
    void setUp() {
        loginControllerUnderTest = new LoginController();
    }

    @Test
    void testLoginResponseEntity() {
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        // Run the test
        final Response actualValue = loginControllerUnderTest.login(loginDTO);

        // Verify the results
        assertEquals("nader te bepalen aanroep naar loginDAO", actualValue.getEntity());
    }

    @Test
    void testLoginResponseStatus() {
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        // Run the test
        final Response actualValue = loginControllerUnderTest.login(loginDTO);

        // Verify the results
        assertEquals(HTTP_STATUS_CREATED, actualValue.getStatus());
    }
}
