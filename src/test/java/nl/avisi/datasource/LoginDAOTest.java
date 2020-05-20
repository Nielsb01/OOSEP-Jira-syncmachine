package nl.avisi.datasource;

import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.UserDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class LoginDAOTest {
    private LoginDAO sut;
    private IDataMapper mockedIDataMapper;
    private Database mockedDatabase;


    @BeforeEach
    void setUp() {
        sut = new LoginDAO();
        mockedIDataMapper = mock(IDataMapper.class);
        mockedDatabase = mock(Database.class);

        sut.setUserDataMapper(mockedIDataMapper);
        sut.setDatabase(mockedDatabase);

    }

    @Test
    void testgetLoginInfoThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedIDataMapper.toDTO(mockedResultSet)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.getLoginInfo("username"));
    }

    @Test
    void testgetLoginInfoCallsToDTO() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedIDataMapper.toDTO(mockedResultSet)).thenReturn(new UserDTO());

        // Act
        UserDTO actualValue = sut.getLoginInfo("username");

        // Assert
        verify(mockedIDataMapper).toDTO(mockedResultSet);
    }

    @Test
    void testgetLoginInfoReturnsCorrectUserDTO() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedIDataMapper.toDTO(mockedResultSet)).thenReturn(
                new UserDTO()
                        .setUserID(1)
                        .setPassword("password")
                        .setUsername("username"));

        // Act
        UserDTO actualValue = sut.getLoginInfo("username");

        // Assert
        assertEquals(1, actualValue.getUserID());
        assertEquals("password", actualValue.getPassword());
        assertEquals("username", actualValue.getUsername());
    }
}
