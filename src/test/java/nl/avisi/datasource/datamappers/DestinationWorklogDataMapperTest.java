package nl.avisi.datasource.datamappers;

import nl.avisi.dto.DestinationWorklogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DestinationWorklogDataMapperTest {

    private DestinationWorklogDataMapper sut;
    private ResultSet mockedResultset;

    @BeforeEach
    void setUp() {
        sut = new DestinationWorklogDataMapper();
        mockedResultset = mock(ResultSet.class);
    }

    @Test
    void testToDTOReturnsDestinationWorklogDTO() throws Exception {
        //Arrange

        //Act
        final DestinationWorklogDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertThat(result, instanceOf(DestinationWorklogDTO.class));
    }

    @Test
    void testToDTOThrowsSQLException() throws SQLException {
        //Arrange
        when(mockedResultset.getInt(anyString())).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(SQLException.class, () -> {
            sut.toDTO(mockedResultset);
        });
    }

    @Test
    void testToDTOReturnsDTOWithCorrectValues() throws SQLException {
        //Arrange
        int timeSpentSeconds = 3600;
        String worker = "worker";
        String originTaskId = "originTaskId";
        String started = "started";

        when(mockedResultset.getInt(anyString())).thenReturn(timeSpentSeconds);
        when(mockedResultset.getString(anyString())).thenReturn(worker, started, originTaskId);

        //Act
        DestinationWorklogDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertEquals(result.getTimeSpentSeconds(), timeSpentSeconds);
        assertEquals(result.getWorker(), worker);
        assertEquals(result.getOriginTaskId(), originTaskId);
        assertEquals(result.getStarted(), started);
    }
}
