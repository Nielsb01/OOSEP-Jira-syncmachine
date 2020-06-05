package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DatabaseLoggerDAOTest {

    public static final String CLASS_NAME = "class_name";
    private DatabaseLoggerDAO sut;
    private Database mockedDatabase;
    private PreparedStatement mockedStatement;
    private Exception mockedException;

    @BeforeEach
    void setUp() {
        sut = new DatabaseLoggerDAO();
        mockedDatabase = mock(Database.class);
        mockedStatement = mock(PreparedStatement.class);
        mockedException = mock(Exception.class);

        sut.setDatabase(mockedDatabase);
    }

    @Test
    void testInsertLogIntoDatabaseClosesConnection() throws Exception {
        //Arrange

        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);


        //Act
        sut.insertLogIntoDatabase(CLASS_NAME, mockedException);

        //Assert
        verify(mockConnection).close();
    }

    @Test
    void testInsertLogIntoDatabaseThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.insertLogIntoDatabase(CLASS_NAME, mockedException));
    }
}
