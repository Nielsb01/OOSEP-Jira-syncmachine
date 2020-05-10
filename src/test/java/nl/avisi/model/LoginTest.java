package nl.avisi.model;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.dto.LoginDTO;
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

    @Test
    void testValidateCredentialsCallsGetLoginInfoWithCorrectParameter() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");

        //Act
        sut.validateCredentials(loginDTO);

        //Assert
        verify(mockedLoginDAO).getLoginInfo(loginDTO.getUsername());
    }

    @Test
    void testValidateCredentialsThrowsExceptionWhenPasswordsDontMatch() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");

        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new LoginDTO().setUsername("ruben").setPassword("wrong password"));

        //Act & Assert
        Assertions.assertThrows(NotAuthorizedException.class, () -> sut.validateCredentials(loginDTO));
    }

    @Test
    void testValidateCredentialsReturnsWhenPasswordMatches() {
        //Arrange
        final LoginDTO loginDTO = new LoginDTO().setUsername("username").setPassword("password");

        when(mockedLoginDAO.getLoginInfo("username"))
                .thenReturn(new LoginDTO().setUsername("ruben").setPassword("dc00c903852bb19eb250aeba05e534a6d211629d77d055033806b783bae09937"));

        //Act
        String actualValue = sut.validateCredentials(loginDTO);

       //Assert
        assertEquals("Succes", actualValue);
    }
}
