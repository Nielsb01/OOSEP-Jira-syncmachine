package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.logger.ILogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class WorklogDAOTest {

    private static final String WORKER = "worker";
    private static final String STARTED = "started";
    private static final int TIME_SPENT_SECONDS = 3600;
    private static final String ORIGIN_TASK_ID = "originTaskId";

    private WorklogDAO sut;
    private Database mockedDatabase;
    private ILogger mockedLogger;
    private IDataMapper mockedWorklogIdDataMapper;
    private IDataMapper mockedWorklogMapper;
    private PreparedStatement mockedStatement;
    private ResultSet mockedResultSet;

    private DestinationWorklogDTO destinationWorklogDTO;
    private static final int WORKLOG_ID = 1;


    @BeforeEach
    void setUp() {
        sut = new WorklogDAO();
        mockedDatabase = mock(Database.class);
        mockedLogger = mock(ILogger.class);
        mockedWorklogIdDataMapper = mock(IDataMapper.class);
        mockedStatement = mock(PreparedStatement.class);
        mockedResultSet = mock(ResultSet.class);
        mockedWorklogMapper = mock(IDataMapper.class);

        sut.setDatabase(mockedDatabase);
        sut.setLogger(mockedLogger);
        sut.setWorklogIdDataMapper(mockedWorklogIdDataMapper);
        sut.setDestinationWorklogMapper(mockedWorklogMapper);

        destinationWorklogDTO = new DestinationWorklogDTO(WORKER, STARTED, TIME_SPENT_SECONDS, ORIGIN_TASK_ID);
    }

    @Test
    void testAddWorklogIdClosesConnection() throws Exception {
        //Arrange

        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);


        //Act
        sut.addWorklogId(0);

        //Assert
        verify(mockConnection).close();
    }

    @Test
    void testAddWorklogIdThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.addWorklogId(0));
    }

    @Test
    void testGetAllWorklogIds() throws Exception {
        //Arrange
        final List<Integer> expectedResult = Arrays.asList(0, 1, 2);

        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedWorklogIdDataMapper.toDTO(any(ResultSet.class))).thenReturn(expectedResult);

        //Act
        final List<Integer> actualResult = sut.getAllWorklogIds();

        //Assert
        assertEquals(expectedResult, actualResult);
        verify(mockConnection).close();
    }

    @Test
    void testGetAllWorklogIdsThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.getAllWorklogIds());
    }

    @Test
    void testAddFailedWorklogThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.addFailedworklog(WORKLOG_ID, destinationWorklogDTO));
    }

    @Test
    void testAddFailedWorklogSetsCorrectStatement() throws Exception {
        //Arrange
        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);

        //Act
        sut.addFailedworklog(WORKLOG_ID, destinationWorklogDTO);

        //Assert
        verify(mockedStatement).setInt(1, WORKLOG_ID);
        verify(mockedStatement).setString(2, WORKER);
        verify(mockedStatement).setString(3, STARTED);
        verify(mockedStatement).setInt(4, TIME_SPENT_SECONDS);
        verify(mockedStatement).setString(5, ORIGIN_TASK_ID);
        verify(mockedStatement).executeUpdate();
    }

    @Test
    void testGetAllFailedWorklogsThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.getAllFailedWorklogs());
    }

    @Test
    void testGetAllFailedWorklogsReturnsCorrectMap() throws SQLException {
        //Arrange
        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true, false);
        when(mockedResultSet.getInt(anyString())).thenReturn(WORKLOG_ID);
        when(mockedWorklogMapper.toDTO(mockedResultSet)).thenReturn(destinationWorklogDTO);

        //Act
        Map<Integer, DestinationWorklogDTO> result = sut.getAllFailedWorklogs();

        //Assert
        assertEquals(result.get(WORKLOG_ID), destinationWorklogDTO);
        assertEquals(result.size(), 1);
    }

    @Test
    void testDeleteFailedworklogThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.deleteFailedWorklog(WORKLOG_ID));
    }

    @Test
    void testDeleteFailedworklogSetsCorrectStatement() throws Exception {
        //Arrange
        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);

        //Act
        sut.deleteFailedWorklog(WORKLOG_ID);

        //Assert
        verify(mockedStatement).setInt(1, WORKLOG_ID);
        verify(mockedStatement).executeUpdate();
    }

}
