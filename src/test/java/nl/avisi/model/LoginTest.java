package nl.avisi.model;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.dto.LoginDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotAuthorizedException;

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
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");

        // Run the test
        sut.validateCredentials(loginDTO);

        // Verify the results
        verify(mockedLoginDAO).getLoginInfo(loginDTO.getUsername());
    }

    @Test
    void testValidateCredentialsThrowsException() {
        // Setup
        final LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("username");
        loginDTO.setPassword("password");
        when(mockedLoginDAO.getLoginInfo("ruben")).thenReturn(new LoginDTO("nebur", "droowthcaw"));


        // Run the test
        // Verify the results
        Assertions.assertThrows(NotAuthorizedException.class, () -> loginDomainUnderTest.validateCredentials(loginDTO));
    }
}
