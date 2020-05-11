package nl.avisi.datasource;

import nl.avisi.datasource.datamappers.DataMapper;
import nl.avisi.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginDAOTest {

    private LoginDAO loginDAOUnderTest;
    private DataMapper

    @BeforeEach
    void setUp() {
        loginDAOUnderTest = new LoginDAO();
    }

    @Test
    void testGetLoginInfo() {
        //Arrange

        //Act
        final UserDTO result = loginDAOUnderTest.getLoginInfo("username");

        //Assert
    }
}
