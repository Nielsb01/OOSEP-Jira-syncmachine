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
    void testGetConnectionStringReturnsPropertyReaderGetProperty() {
        // Arrange
        String connection_property_name = "connection";
        String expected = "connection";

        Mockito.when(mockedPropertyReader.getProperty(connection_property_name)).thenReturn(expected);

        // Act
        String actual = sut.getConnectionString();

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
}
