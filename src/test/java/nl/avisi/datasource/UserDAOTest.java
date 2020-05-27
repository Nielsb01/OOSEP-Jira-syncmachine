package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.datasource.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.dto.UserPreferenceDTO;
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
    public static final int USER_ID = 1;
    public static final String JIRAUSER_1010 = "JIRAUSER1010";
    public static final String JIRAUSER_1000 = "JIRAUSER1000";
    private UserDAO sut;
    private Database mockedDatabase;
    private IDataMapper<UserSyncDTO> mockedUserSyncDataMapper;
    private IDataMapper<UserPreferenceDTO> mockedUserPreferenceMapper;

    @BeforeEach
    void setUp() {
        sut = new UserDAO();
        mockedDatabase = mock(Database.class);
        mockedUserSyncDataMapper = mock(IDataMapper.class);
        mockedUserPreferenceMapper = mock(IDataMapper.class);

        sut.setDatabase(mockedDatabase);
        sut.setUserSyncDataMapper(mockedUserSyncDataMapper);
        sut.setUserPreferenceDataMapper(mockedUserPreferenceMapper);
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
    void testGetAllAutoSyncUsersReturnsListWithObjectsFromDatabase() throws SQLException, DatabaseDriverNotFoundException {
        // Arrange
        UserSyncDTO userSyncDTO = new UserSyncDTO(JIRAUSER_1010,JIRAUSER_1000);

        UserSyncDTO userSyncDTO1 = new UserSyncDTO(JIRAUSER_1010,JIRAUSER_1000);

        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true, true, false);
        when(mockedUserSyncDataMapper.toDTO(mockedResultSet)).thenReturn(userSyncDTO, userSyncDTO1);

        // Act
        List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

        // Assert
        assertEquals(userSyncDTO, results.get(0));
        assertEquals(userSyncDTO1, results.get(1));
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

    @Test
    void testGetSyncUserReturnsCorrectObject() throws SQLException {
        //Arrange
        UserSyncDTO userSyncDTO = new UserSyncDTO(JIRAUSER_1010,JIRAUSER_1000);

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedUserSyncDataMapper.toDTO(mockedResultSet)).thenReturn(userSyncDTO);

        //Act
        UserSyncDTO result = sut.getSyncUser(USER_ID);

        //Assert
        assertEquals(JIRAUSER_1000, result.getDestinationWorker());
        assertEquals(JIRAUSER_1010, result.getOriginWorker());
    }

    @Test
    void testGetSyncUserThrowsInternalServerErrorWhenSQLExceptionIsThrown() throws SQLException {
        //Arrange
        UserSyncDTO userSyncDTO = new UserSyncDTO(JIRAUSER_1010,JIRAUSER_1000);

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedDatabase.connect()).thenThrow(SQLException.class);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedUserSyncDataMapper.toDTO(mockedResultSet)).thenReturn(userSyncDTO);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.getSyncUser(USER_ID));
    }

    @Test
    void testGetSyncUserCallsDataMapper() throws SQLException {
        //Arrange
        UserSyncDTO userSyncDTO = new UserSyncDTO(JIRAUSER_1010,JIRAUSER_1000);

        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedUserSyncDataMapper.toDTO(mockedResultSet)).thenReturn(userSyncDTO);

        //Act
        UserSyncDTO result = sut.getSyncUser(USER_ID);

        //Assert
        verify(mockedUserSyncDataMapper).toDTO(mockedResultSet);
    }

    @Test
    void testUpdateJiraUserKeysThrowsInternalServerErrorWhenSQLExceptionIsThrown() throws SQLException {
        JiraUserKeyDTO jiraUserKeyDTO = new JiraUserKeyDTO(JIRAUSER_1000,JIRAUSER_1010);

        //Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenThrow(SQLException.class);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.updateJiraUserKeys(jiraUserKeyDTO, USER_ID));
    }

    @Test
    void testUpdateJiraUserKeysThrowsCallsConnectAndClose() throws SQLException {
        JiraUserKeyDTO jiraUserKeyDTO = new JiraUserKeyDTO(JIRAUSER_1000,JIRAUSER_1010);

        //Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        //Act
        sut.updateJiraUserKeys(jiraUserKeyDTO, USER_ID);

        //Assert
        verify(mockedStatement).setString(1, JIRAUSER_1000);
        verify(mockedStatement).setString(2, JIRAUSER_1010);
        verify(mockedStatement).setInt(3, USER_ID);
    }

    @Test
    void testUpdateJiraUserKeysMakesConnectionAndClosesIt() throws SQLException {
        JiraUserKeyDTO jiraUserKeyDTO = new JiraUserKeyDTO(JIRAUSER_1000,JIRAUSER_1010);

        //Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        //Act
        sut.updateJiraUserKeys(jiraUserKeyDTO, USER_ID);

        //Assert
        verify(mockedConnection).close();
        verify(mockedDatabase).connect();
        verify(mockedStatement).close();
    }

    @Test
    void testGetUserAutoSyncPreferenceThrowsInternalServerErrorSqlExceptionIsThrown() throws SQLException {
        // Arrange
        final int userId = 0;
        when(mockedDatabase.connect()).thenThrow(new SQLException());

        // Act & assert
        assertThrows(InternalServerErrorException.class, () -> {
            sut.getUserAutoSyncPreference(userId);
        });
    }

    @Test
    void testGetUserAutoSyncPreferenceReturnsTrueWhenDataIsReturned() throws SQLException {
        // Arrange
        final boolean autoSyncOn = true;
        final UserPreferenceDTO expectedResult = new UserPreferenceDTO(autoSyncOn);

        Connection mockedConnection = mock(Connection.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.next()).thenReturn(true, false);
        when(mockedResultSet.getBoolean(anyString())).thenReturn(autoSyncOn);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedUserPreferenceMapper.toDTO(any())).thenReturn(expectedResult);

        // Act
        UserPreferenceDTO result = sut.getUserAutoSyncPreference(0);

        // Assert
        assertTrue(result.getAutoSyncOn());
    }
}
