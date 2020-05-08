package propertyReaders;

import nl.avisi.propertyReaders.JiraSynchronisationProperties;
import nl.avisi.propertyReaders.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class JiraSynchronisationPropertiesTest {

    JiraSynchronisationProperties sut;
    PropertyReader mockedPropertyReader;

    @BeforeEach
    void setup() {
        sut = new JiraSynchronisationProperties();

        mockedPropertyReader = Mockito.mock(PropertyReader.class);
        sut.setPropertyReader(mockedPropertyReader);
    }

    @Test
    void testLoadPropertiesCallsPropertyReaderLoadProperties() {
        // Arrange

        // Act
        sut.loadPropertyFile();

        // Assert
        Mockito.verify(mockedPropertyReader).loadPropertyFile("jiraSynchronisation.properties");
    }

    @Test
    void testGetOriginUrlReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "ORIGIN";
        Mockito.when(mockedPropertyReader.getProperty("originUrl")).thenReturn(expected);

        // Act
        String actual = sut.getOriginUrl();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetDestinationUrlReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "DESTINATION";
        Mockito.when(mockedPropertyReader.getProperty("destinationUrl")).thenReturn(expected);

        // Act
        String actual = sut.getDestinationUrl();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetAdminUsernameReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "USERNAME";
        Mockito.when(mockedPropertyReader.getProperty("adminUsername")).thenReturn(expected);

        // Act
        String actual = sut.getAdminUsername();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetAdminPasswordReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "PASSWORD";
        Mockito.when(mockedPropertyReader.getProperty("adminPassword")).thenReturn(expected);

        // Act
        String actual = sut.getAdminPassword();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationMomentReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "MOMENT";
        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(expected);

        // Act
        String actual = sut.getSynchronisationMoment();

        // Assert
        assertEquals(expected, actual);
    }
}
