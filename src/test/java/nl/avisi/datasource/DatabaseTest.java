package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.propertyreaders.DatabaseProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseTest {
    private Database sut;
    private DatabaseProperties mockedDatabaseProperties;

    @BeforeEach
    void setUp() {
        sut = new Database();
        mockedDatabaseProperties = mock(DatabaseProperties.class);

        sut.setDatabaseProperties(mockedDatabaseProperties);
    }

    @Test
    void testConnectThrowsInternalServerErrorWhenTheDriverClassDoesNotExist() {
        // Arrange
        when(mockedDatabaseProperties.getDriverName()).thenReturn("");

        // Assert
        assertThrows(DatabaseDriverNotFoundException.class, () -> {

            // Act
            sut.connect();
        });
    }
}
