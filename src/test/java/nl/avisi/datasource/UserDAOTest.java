package nl.avisi.datasource;

import nl.avisi.propertyreaders.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.dto.UserSyncDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    private UserDAO sut;
    private Database mockedDatabase;

    @BeforeEach
    void setUp() {
        sut = new UserDAO();
        mockedDatabase = mock(Database.class);

        sut.setDatabase(mockedDatabase);
    }

    @Test
    void testGetAllAutoSyncUsersReturnsEmptyListWhenNoResultsAreFound() throws SQLException, DatabaseDriverNotFoundException {
        // Arrange
        final int expectedResultSize = 0;

        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(false);

        // Act
        List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

        // Assert
        assertEquals(expectedResultSize, results.size());
    }

    @Test
    void testGetAllAutoSyncUsersReturnsEmptyListWhenSQLExceptionIsThrown() throws SQLException, DatabaseDriverNotFoundException {
        // Arrange
        final int expectedResultSize = 0;

        when(mockedDatabase.connect()).thenThrow(new SQLException());

        // Act
        List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

        // Assert
        assertEquals(expectedResultSize, results.size());
    }

    @Test
    void testGetAllAutoSyncUsersReturnsEmptyListWhenDatabaseDriverNotFoundExceptionIsThrown() throws SQLException, DatabaseDriverNotFoundException {
        // Arrange
        final int expectedResultSize = 0;

        when(mockedDatabase.connect()).thenThrow(new DatabaseDriverNotFoundException(""));

        // Act
        List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

        // Assert
        assertEquals(expectedResultSize, results.size());
    }

    @Test
    void testGetAllAutoSyncUsersReturnsListWithObjectsFromDatabase() throws SQLException, DatabaseDriverNotFoundException {
        // Arrange
        final String firstSyncUserFromWorker = "from1";
        final String firstSyncUserToWorker = "to1";
        final String secondSyncUserFromWorker = "from2";
        final String secondSyncUserToWorker = "to2";

        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true, true, false);
        when(mockedResultSet.getString(anyString())).thenReturn(firstSyncUserFromWorker,
                firstSyncUserToWorker,
                secondSyncUserFromWorker,
                secondSyncUserToWorker);

        // Act
        List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

        // Assert
        assertEquals(firstSyncUserFromWorker, results.get(0).getOriginWorker());
        assertEquals(firstSyncUserToWorker, results.get(0).getDestinationWorker());
        assertEquals(secondSyncUserFromWorker, results.get(1).getOriginWorker());
        assertEquals(secondSyncUserToWorker, results.get(1).getDestinationWorker());
    }

    @Test
    void testSetAutoSyncPreferenceCallsConnect() throws SQLException {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        //Act
        sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        verify(mockedDatabase).connect();
    }

    @Test
    void testSetAutoSyncPreferenceClosesConnectionAndStatement() throws SQLException {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        //Act
        sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        verify(mockedConnection).close();
        verify(mockedStatement).close();
    }

    @Test
    void testSetAutoSyncPreferenceSetsPreparedStmtCorrectly() throws SQLException {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        //Act
        sut.setAutoSyncPreference(userId, autoSyncOn);

        //Assert
        verify(mockedStatement).setBoolean(1, autoSyncOn);
        verify(mockedStatement).setInt(2, userId);
        
    }

    @Test
    void testSetAutoSyncPreferenceThrowsInternalServerErrorWhenSQLExceptionIsThrown() throws SQLException {
        //Arrange
        final int userId = 1;
        final boolean autoSyncOn = true;

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenThrow(SQLException.class);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        //Act & Assert
      assertThrows(InternalServerErrorException.class, () -> sut.setAutoSyncPreference(userId, autoSyncOn));
    }
}
