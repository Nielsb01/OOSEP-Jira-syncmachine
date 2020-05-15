package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserDTO;
import nl.avisi.dto.UserSyncDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserSyncDataMapperTest {
    public static final String JIRAUSER_1010 = "JIRAUSER1010";
    public static final String JIRAUSER_1000 = "JIRAUSER1000";
    private UserSyncDataMapper sut;
    private ResultSet mockedResultset;

    @BeforeEach
    void setUp() {
        sut = new UserSyncDataMapper();
        mockedResultset = mock(ResultSet.class);
    }

    @Test
    void testToDTOReturnsUserSyncDTO() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(false);

        //Act
        final UserSyncDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertThat(result, instanceOf(UserSyncDTO.class));
    }

    @Test
    void testToDTOThrowsSQLException() throws SQLException {
        //Arrange
        when(mockedResultset.getString(any())).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(SQLException.class, () -> {
            sut.toDTO(mockedResultset);
        });
    }

    @Test
    void testToDTOReturnsUserDTOWithCorrectValues() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(true);
        when(mockedResultset.getString(any())).thenReturn(JIRAUSER_1000, JIRAUSER_1010);

        //Act
        final UserSyncDTO result = sut.toDTO(mockedResultset);

        //Assert
        assertEquals(JIRAUSER_1000, result.getOriginWorker());
        assertEquals(JIRAUSER_1010, result.getDestinationWorker());
    }

    @Test
    void testToDTOCallsGetStringTwice() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(true);

        //Act
        final UserSyncDTO result = sut.toDTO(mockedResultset);

        //Assert
        verify(mockedResultset, times(2)).getString(any());
    }
}
