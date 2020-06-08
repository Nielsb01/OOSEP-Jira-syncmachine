package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class WorklogDAOTest {

    private WorklogDAO sut;
    private Database mockedDatabase;
    private ILogger mockedLogger;
    private IDataMapper mockedDataMapper;
    private PreparedStatement mockedStatement;

    @BeforeEach
    void setUp() {
        sut = new WorklogDAO();
        mockedDatabase = mock(Database.class);
        mockedLogger = mock(ILogger.class);
        mockedDataMapper = mock(IDataMapper.class);
        mockedStatement = mock(PreparedStatement.class);

        sut.setDatabase(mockedDatabase);
        sut.setLogger(mockedLogger);
        sut.setWorklogIdDataMapper(mockedDataMapper);
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
        when(mockedDataMapper.toDTO(any(ResultSet.class))).thenReturn(expectedResult);

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
}
