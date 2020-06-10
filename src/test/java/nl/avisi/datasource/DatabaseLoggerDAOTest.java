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

    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String ERROR_MESS = "errorMess";
    private DatabaseLoggerDAO sut;
    private Database mockedDatabase;
    private PreparedStatement mockedStatement;

    @BeforeEach
    void setUp() {
        sut = new DatabaseLoggerDAO();
        mockedDatabase = mock(Database.class);
        mockedStatement = mock(PreparedStatement.class);
        sut.setDatabase(mockedDatabase);
    }

    @Test
    void testInsertLogIntoDatabaseClosesConnection() throws Exception {
        //Arrange
        SQLException exception = new SQLException();
        final Connection mockConnection = mock(Connection.class);
        when(mockedDatabase.connect()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockedStatement);

        //Act
        sut.insertLogIntoDatabase(CLASS_NAME, METHOD_NAME, ERROR_MESS);

        //Assert
        verify(mockedStatement).setString(1, CLASS_NAME);
        verify(mockedStatement).setString(2, METHOD_NAME);
        verify(mockedStatement).setString(3, ERROR_MESS);
        verify(mockConnection).close();
    }

    @Test
    void testInsertLogIntoDatabaseThrowsInternalServerErrorExceptionWhenSQLExceptionIsThrown() throws Exception {
        //Arrange
        when(mockedDatabase.connect()).thenThrow(SQLException.class);

        //Act & Assert
        assertThrows(InternalServerErrorException.class, () -> sut.insertLogIntoDatabase(CLASS_NAME, METHOD_NAME, ERROR_MESS));
    }
}
