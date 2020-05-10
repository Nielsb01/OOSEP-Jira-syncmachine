package nl.avisi.datasource;

import nl.avisi.datasource.datamappers.DataMapper;
import nl.avisi.dto.LoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LoginDAOTest {

    private LoginDAO sut;
    private Database mockedDatabase;
    private DataMapper mockedLoginDataMapper;

    @BeforeEach
    void setUp() {
        sut = new LoginDAO();
        mockedDatabase = Mockito.mock(Database.class);
        mockedLoginDataMapper = (DataMapper.class);

        sut.setDatabase(mockedDatabase);
        sut.setLoginDataMapper(mockedLoginDataMapper);
    }

    @Test
    void testGetLoginInfo() {
        // Setup

        // Run the test
        final LoginDTO result = sut.getLoginInfo("username");

        // Verify the results
    }
}
