package nl.avisi.controller;

import nl.avisi.dto.LoginDTO;
import nl.avisi.model.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    public static final int HTTP_STATUS_OK = 200;
    public static final int USER_ID = 1;
    private LoginController sut;
    private Login mockedLogin;

    @BeforeEach
    void setUp() {
        sut = new LoginController();
        mockedLogin = mock(Login.class);
        sut.setLogin(mockedLogin);
    }

 /*   @Test
    void testLoginResponseEntity() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        when(mockedLogin.validateCredentials(loginDTO)).thenReturn(USER_ID);

        //Act
        final Response actualValue = sut.login(loginDTO);

        //Assert
        assertEquals(USER_ID, actualValue.getEntity());
    }

    @Test
    void testLoginResponseStatus() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        //Act
        final Response actualValue = sut.login(loginDTO);

        //Assert
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }

    @Test
    void testLoginCallsValidateCredentials() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        //Act
        final Response actualValue = sut.login(loginDTO);

        //Assert
        verify(mockedLogin).validateCredentials(loginDTO);
    }*/
}
