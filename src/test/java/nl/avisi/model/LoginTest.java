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

    private final LoginDTO LOGIN_DTO = new LoginDTO("username", "password");

    @BeforeEach
    void setUp() {
        sut = new Login();
        mockedLoginDAO = mock(ILoginDAO.class);

        sut.setLoginDAO(mockedLoginDAO);

    }

    @Test
    void testValidateCredentialsCallsGetLoginInfoWithCorrectParameter() {
        //Arrange
        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO(1, "username", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));

        //Act
        int actualValue = sut.validateCredentials(LOGIN_DTO);

        //Assert
        verify(mockedLoginDAO).getLoginInfo(LOGIN_DTO.getUsername());
    }

    @Test
    void testValidateCredentialsThrowsExceptionWhenPasswordsDontMatch() {
        //Arrange
        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO(1, "username", "wrong password"));

        //Act & Assert
        Assertions.assertThrows(NotAuthorizedException.class, () -> sut.validateCredentials(LOGIN_DTO));
    }

    @Test
    void testValidateCredentialsReturnsUserIdWhenPasswordMatches() {
        //Arrange
        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new UserDTO(
                        1,
                        "username",
                        "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));

        //Act
        int actualValue = sut.validateCredentials(LOGIN_DTO);

        //Assert
        assertEquals(1, actualValue);
    }
}
