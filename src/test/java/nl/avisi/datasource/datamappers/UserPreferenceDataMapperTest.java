package nl.avisi.datasource.datamappers;
import nl.avisi.dto.UserPreferenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserPreferenceDataMapperTest {

    private UserPreferenceDataMapper sut;
    private ResultSet mockedResultset;

    @BeforeEach
    void setUp() {
        sut = new UserPreferenceDataMapper();
        mockedResultset = mock(ResultSet.class);
    }

    @Test
    void testToDTOReturnsUserPreferenceDTO() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(false);

        //Act
        final UserPreferenceDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertThat(result, instanceOf(UserPreferenceDTO.class));
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
    void testToDTOReturnsUserDTOWithCorrectValues() throws Exception {
        //Arrange
        boolean autoSyncOn = true;

        when(mockedResultset.next()).thenReturn(true);
        when(mockedResultset.getBoolean(any())).thenReturn(autoSyncOn);

        //Act
        final UserPreferenceDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertEquals(autoSyncOn, result.getAutoSyncOn());
    }

    @Test
    void testToDTOCallsNextResultsetOnlyOnce() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(true);

        //Act
        sut.toDTO(mockedResultset);

        //Assert
        verify(mockedResultset, times(1)).next();
    }
}
