package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.exceptions.LastSynchronisationDateNotFoundException;
import nl.avisi.logger.ILogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AutomaticSynchronisationDAOTest {
    private AutomaticSynchronisationDAO sut;
    private Database mockedDatabase;

    private static final String MOMENT = "2020-06-02 12:00:00";


    @BeforeEach
    void setUp() {
        sut = new AutomaticSynchronisationDAO();

        mockedDatabase = mock(Database.class);
        sut.setDatabase(mockedDatabase);
    }

    @Test
    void testGetLastSynchronisationMomentThrowsInternalServerErrorWhenSQLExceptionIsThrown() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true);
        when(mockedResultSet.getString(anyString())).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.getLastSynchronisationMoment());
    }

    @Test
    void testGetLastSynchronisationMomentReturnsCorrectString() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true);
        when(mockedResultSet.getString(anyString())).thenReturn(MOMENT);

        // Act
        String actual = sut.getLastSynchronisationMoment();

        // Assert
        assertEquals(MOMENT, actual);
    }

    @Test
    void testGetLastSynchronisationMomentCallsResultSetGetStringWithCorrectParameter() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true);

        // Act
        sut.getLastSynchronisationMoment();

        // Assert
        Mockito.verify(mockedResultSet).getString("synchronisation_moment");
    }

    @Test
    void testSetLastSynchronisationMomentCallsConnect() throws SQLException {
        // Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        // Act
        sut.setLastSynchronisationMoment(MOMENT);

        // Assert
        verify(mockedDatabase).connect();
    }

    @Test
    void testSetLastSynchronisationMomentClosesConnectionAndStatement() throws SQLException {
        // Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        // Act
        sut.setLastSynchronisationMoment(MOMENT);

        // Assert
        verify(mockedConnection).close();
        verify(mockedStatement).close();
    }

    @Test
    void testSetLastSynchronisationMomentSetsPreparedStatementCorrectly() throws SQLException {
        // Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);


        when(mockedDatabase.connect()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        // Act
        sut.setLastSynchronisationMoment(MOMENT);

        // Assert
        verify(mockedStatement).setString(1, MOMENT);

    }

    @Test
    void testSetLastSynchronisationMomentThrowsInternalServerErrorWhenSQLExceptionIsThrown() throws SQLException {
        // Arrange
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenThrow(SQLException.class);
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);

        // Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.setLastSynchronisationMoment(MOMENT));
    }

    @Test
    void testGetLastSynchronisationDateThrowsLastSynchronisationDateNotFoundException() throws SQLException {
        // Arrange
        ResultSet mockedResultSet = mock(ResultSet.class);
        PreparedStatement mockedStatement = mock(PreparedStatement.class);
        Connection mockedConnection = mock(Connection.class);

        when(mockedDatabase.connect()).thenReturn(mockedConnection);;
        when(mockedConnection.prepareStatement(any())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(false);

        // Act & Assert
        assertThrows(LastSynchronisationDateNotFoundException.class, () -> sut.getLastSynchronisationMoment());

    }
}
