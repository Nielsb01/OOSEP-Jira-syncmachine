package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;
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
    private IDataMapper mockedDataMapper;
    private PreparedStatement mockedStatement;

    @BeforeEach
    void setUp() {
        sut = new WorklogDAO();
        mockedDatabase = mock(Database.class);
        mockedDataMapper = mock(IDataMapper.class);
        mockedStatement = mock(PreparedStatement.class);

        sut.setDatabase(mockedDatabase);
        sut.setWorklogIdDataMapper(mockedDataMapper);
    }

    @Test
    void testAddWorklogIdClosesConnection() throws Exception {
        // Setup

        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);


        // Run the test
        sut.addWorklogId(0);

        // Verify the results
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
