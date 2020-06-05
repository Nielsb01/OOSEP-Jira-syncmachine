package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;


class DatabaseLoggerDAOTest {

    private DatabaseLoggerDAO sut;
    private Database mockedDatabase;

    @BeforeEach
    void setUp() {
        sut = new DatabaseLoggerDAO();
        mockedDatabase = mock(Database.class);

        sut.setDatabase(mockedDatabase);
    }

}
