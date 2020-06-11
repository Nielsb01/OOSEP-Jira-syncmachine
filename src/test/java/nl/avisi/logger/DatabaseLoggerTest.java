package nl.avisi.logger;

import nl.avisi.datasource.DatabaseLoggerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DatabaseLoggerTest {

    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String ERROR_MESS = "errorMess";
    private DatabaseLogger sut;
    private DatabaseLoggerDAO mockedDAO;
    private Exception mockedException;

    @BeforeEach
    void setUp() {
        sut = new DatabaseLogger();
        mockedDAO = mock(DatabaseLoggerDAO.class);
        sut.setDatabaseLoggerDAO(mockedDAO);
        mockedException = new Exception(ERROR_MESS);
    }

    @Test
    void TestGetErrorMessageFromException(){
        //Arrange
        //Act
        sut.logToDatabase(CLASS_NAME, METHOD_NAME, mockedException);
        //Assert
        verify(mockedDAO).insertLogIntoDatabase(CLASS_NAME, METHOD_NAME, ERROR_MESS);
    }
}
