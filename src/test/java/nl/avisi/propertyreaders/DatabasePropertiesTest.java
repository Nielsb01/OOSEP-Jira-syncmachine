package nl.avisi.propertyreaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

public class DatabasePropertiesTest {

    DatabaseProperties sut;
    PropertyReader mockedPropertyReader;

    @BeforeEach
    void setup() {
        sut = new DatabaseProperties();

        mockedPropertyReader = Mockito.mock(PropertyReader.class);
        sut.setPropertyReader(mockedPropertyReader);
    }

    @Test
    void testSetPropertyReaderCallsPropertyReaderLoadProperties() {
        // Arrange

        // Act

        // Assert
        Mockito.verify(mockedPropertyReader).loadPropertyFile(anyString());
    }

    @Test
    void testGetDatabaseUrlReturnsPropertyReaderGetProperty() {
        // Arrange
        String url_property_name = "databaseUrl";
        String expected = "databaseUrl";

        Mockito.when(mockedPropertyReader.getProperty(url_property_name)).thenReturn(expected);

        // Act
        String actual = sut.getDatabaseUrl();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetDriverNameReturnsPropertyReaderGetProperty() {
        // Arrange
        String driver_property_name = "driver";
        String expected = "driver";

        Mockito.when(mockedPropertyReader.getProperty(driver_property_name)).thenReturn(expected);

        // Act
        String actual = sut.getDriverName();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetUsernameReturnsPropertyReaderGetProperty() {
        // Arrange
        String driver_property_name = "username";
        String expected = "username";

        Mockito.when(mockedPropertyReader.getProperty(driver_property_name)).thenReturn(expected);

        // Act
        String actual = sut.getUsername();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetPasswordReturnsPropertyReaderGetProperty() {
        // Arrange
        String driver_property_name = "password";
        String expected = "password";

        Mockito.when(mockedPropertyReader.getProperty(driver_property_name)).thenReturn(expected);

        // Act
        String actual = sut.getPassword();

        // Assert
        assertEquals(expected, actual);
    }
}
