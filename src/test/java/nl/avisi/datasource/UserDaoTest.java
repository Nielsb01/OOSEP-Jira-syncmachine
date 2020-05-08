package nl.avisi.datasource;

import nl.avisi.model.UserSyncDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoTest {
    private UserDao sut;
    private Database mockedDatabase;

    @BeforeEach
    void setUp() {
        sut = new UserDao();
        mockedDatabase = mock(Database.class);

        sut.setDatabase(mockedDatabase);
    }

    @Test
    void testGetAllAutoSyncUsersReturnsEmptyListWhenNoResultsAreFound() { //throws SQLException {
        // Arrange
        final int expectedResultSize = 0;

        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

       try {
           when(mockedDatabase.connect()).thenReturn(mockedConnection);
           when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
           when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
           when(mockedResultSet.next()).thenReturn(false);

           // Act
           List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

           // Assert
           assertEquals(expectedResultSize, results.size());
       } catch (SQLException e) {
           // Explicitly set the assertTrue to false so the
           // test will fail when a SQLException occurs
           assertTrue(false);
       }
    }

    @Test
    void testGetAllAutoSyncUsersReturnsEmptyListWhenSQLExceptionIsThrown() {
        // Arrange
        final int expectedResultSize = 0;

        try {
            when(mockedDatabase.connect()).thenThrow(new SQLException());

            // Act
            List<UserSyncDTO> results = sut.getAllAutoSyncUsers();

            // Assert
            assertEquals(expectedResultSize, results.size());
        } catch (SQLException e) {
            // Explicitly set the assertTrue to false so the
            // test will fail when a SQLException occurs
            assertTrue(false);
        }
    }

    @Test
    void testGetAllAutoSyncUsersReturnsListWithObjectsFromDatabase() {
        // Arrange
        final String firstSyncUserFromWorker = "from1";
        final String firstSyncUserToWorker = "to1";
        final String secondSyncUserFromWorker = "from2";
        final String secondSyncUserToWorker = "to2";

        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        try {
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
            assertEquals(firstSyncUserFromWorker, results.get(0).getFromWorker());
            assertEquals(firstSyncUserToWorker, results.get(0).getToWorker());
            assertEquals(secondSyncUserFromWorker, results.get(1).getFromWorker());
            assertEquals(secondSyncUserToWorker, results.get(1).getToWorker());
        } catch (SQLException e) {
            // Explicitly set the assertTrue to false so the
            // test will fail when a SQLException occurs
            assertTrue(false);
        }
    }

    public void testGetAllAutoSyncUsersDoesntCloseStatementWhenItsNotInitialized() throws SQLException {
        // Arrange
        when(mockedDatabase.connect()).thenThrow(new SQLException());

        // Act
        sut.getAllAutoSyncUsers();

        // Assert
//        verify()
    }

    void testGetAllAutoSyncUsersDoesntCloseConnectionWhenItsNotInitialized() {

    }
}
