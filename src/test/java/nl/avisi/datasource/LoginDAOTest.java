package nl.avisi.datasource;

import nl.avisi.propertyreaders.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.UserDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testgetLoginInfoReturnsEmptyUserDTOWhenSQLExceptionIsThrown() throws SQLException, DatabaseDriverNotFoundException  {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedIDataMapper.toDTO(mockedResultSet)).thenThrow(SQLException.class);

        // Act
        UserDTO actualValue = sut.getLoginInfo("username");

        // Assert
        assertNull(actualValue.getUsername());
        assertEquals(0, actualValue.getUserID());
        assertNull(actualValue.getPassword());
    }

    @Test
    void testgetLoginInfoReturnsEmptyUserDTOWhenDatabaseDriverNotFoundExceptionIsThrown() throws SQLException, DatabaseDriverNotFoundException  {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenThrow(DatabaseDriverNotFoundException.class);

        // Act
        UserDTO actualValue = sut.getLoginInfo("username");

        // Assert
        assertNull(actualValue.getUsername());
        assertEquals(0, actualValue.getUserID());
        assertNull(actualValue.getPassword());
    }

    @Test
    void testgetLoginInfoCallsToDTO() throws SQLException, DatabaseDriverNotFoundException  {
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


}
