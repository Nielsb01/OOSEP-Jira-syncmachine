package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class WorklogIdDataMapperTest {

    private WorklogIdDataMapper sut;
    private ResultSet mockedResultset;

    @BeforeEach
    void setUp() {
        sut = new WorklogIdDataMapper();
        mockedResultset = mock(ResultSet.class);
    }

    @Test
    void testToDTOReturnsList() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(false);
        //Act
        final List<Integer> result = sut.toDTO(mockedResultset);

        //Assert
        assertThat(result, instanceOf(List.class));
    }

    @Test
    void testToDTOThrowsSQLException() throws SQLException {
        //Arrange
        when(mockedResultset.next()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(SQLException.class, () -> {
            sut.toDTO(mockedResultset);
        });
    }

    @Test
    void testToDTOCallsNextResultset() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(true, false);

        //Act
        List<Integer> result = sut.toDTO(mockedResultset);

        //Assert
        verify(mockedResultset, times(2)).next();
    }

    @Test
    void testToDTOReturnsListWithCorrectValues() throws Exception {
        //Arrange
        final int one = 1;
        final int two = 2;
        final int three = 3;
        final int four = 4;

        when(mockedResultset.next()).thenReturn(true, true, true, true, false);
        when(mockedResultset.getInt(any())).thenReturn(one, two, three, four);

        //Act
        final List<Integer> result = sut.toDTO(mockedResultset);

        //Assert
        assertEquals(one, result.get(0).intValue());
        assertEquals(two, result.get(1).intValue());
        assertEquals(three, result.get(2).intValue());
        assertEquals(four, result.get(3).intValue());
    }
}
