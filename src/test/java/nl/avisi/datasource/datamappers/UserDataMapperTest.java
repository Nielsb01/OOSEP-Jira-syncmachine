package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserDataMapperTest {

    private UserDataMapper userDataMapperUnderTest;
    private ResultSet mockedResultset;

    @BeforeEach
    void setUp() {
        userDataMapperUnderTest = new UserDataMapper();
        mockedResultset = mock(ResultSet.class);
    }

    @Test
    void testToDTOReturnsUserDTO() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(false);
        //Act
        final UserDTO result = userDataMapperUnderTest.toDTO(mockedResultset);

        //Assert
        assertThat(result, instanceOf(UserDTO.class));
    }

    @Test
    void testToDTOThrowsSQLException() throws SQLException {
        //Arrange
      when(mockedResultset.next()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(SQLException.class, () -> {
            userDataMapperUnderTest.toDTO(mockedResultset);
        });
    }

    @Test
    void testToDTOReturnsUserDTOWithCorrectValues() throws Exception {
        //Arrange
        int userID = 1;
        String password = "password";
        String username = "username";

        when(mockedResultset.next()).thenReturn(true);
        when(mockedResultset.getString(any())).thenReturn(username, password);
        when(mockedResultset.getInt(any())).thenReturn(userID);
        //Act
        final UserDTO result = userDataMapperUnderTest.toDTO(mockedResultset);

        //Assert
        assertEquals(password, result.getPassword());
        assertEquals(username, result.getUsername());
        assertEquals(userID, result.getUserID());
    }

    @Test
    void testToDTOCallsNextResultsetOnlyOnce() throws Exception {
        //Arrange
        when(mockedResultset.next()).thenReturn(true);

        //Act
        final UserDTO result = userDataMapperUnderTest.toDTO(mockedResultset);

        //Assert
        verify(mockedResultset,times(1)).next();
    }
}
