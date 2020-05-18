package nl.avisi.model;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.dto.LoginDTO;
import nl.avisi.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotAuthorizedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class LoginTest {

    private Login sut;
    private ILoginDAO mockedLoginDAO;

    @BeforeEach
    void setUp() {
        sut = new Login();
        mockedLoginDAO = mock(ILoginDAO.class);

        sut.setLoginDAO(mockedLoginDAO);

    }

   /* @Test
    void testValidateCredentialsCallsGetLoginInfoWithCorrectParameter() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");
        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO().setUsername("username").setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8").setUserID(1));

        //Act
        int actualValue = sut.validateCredentials(loginDTO);

        //Assert
        verify(mockedLoginDAO).getLoginInfo(loginDTO.getUsername());
    }

    @Test
    void testValidateCredentialsThrowsExceptionWhenPasswordsDontMatch() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");

        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO().setUsername("username").setPassword("wrong password").setUserID(1));

        //Act & Assert
        Assertions.assertThrows(NotAuthorizedException.class, () -> sut.validateCredentials(loginDTO));
    }

    @Test
    void testValidateCredentialsReturnsUserIdWhenPasswordMatches() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");

        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO().setUsername("username")
                        .setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8")
                        .setUserID(1));

        //Act
        int actualValue = sut.validateCredentials(loginDTO);

        //Assert
        assertEquals(1, actualValue);
    }*/
}