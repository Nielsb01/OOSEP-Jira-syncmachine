package PropertyReaders;

import nl.avisi.PropertyReaders.JiraSynchronisationProperties;
import nl.avisi.PropertyReaders.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class JiraSynchronisationPropertiesTest {

    JiraSynchronisationProperties sut;
    PropertyReader mockedPropertyReader;

    @BeforeEach
    public void setup() {
        sut = new JiraSynchronisationProperties();

        mockedPropertyReader = Mockito.mock(PropertyReader.class);
        sut.setPropertyReader(mockedPropertyReader);
    }

    @Test
    public void testLoadPropertiesCallsPropertyReaderLoadProperties() {
        // Arrange

        // Act
        sut.loadPropertyFile();

        // Assert
        Mockito.verify(mockedPropertyReader).loadPropertyFile("jiraSynchronisation.properties");
    }

    @Test
    public void testGetOriginUrlReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "ORIGIN";
        Mockito.when(mockedPropertyReader.getProperty("originUrl")).thenReturn(expected);

        // Act
        String actual = sut.getOriginUrl();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDestinationUrlReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "DESTINATION";
        Mockito.when(mockedPropertyReader.getProperty("destinationUrl")).thenReturn(expected);

        // Act
        String actual = sut.getDestinationUrl();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAdminUsernameReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "USERNAME";
        Mockito.when(mockedPropertyReader.getProperty("adminUsername")).thenReturn(expected);

        // Act
        String actual = sut.getAdminUsername();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAdminPasswordReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "PASSWORD";
        Mockito.when(mockedPropertyReader.getProperty("adminPassword")).thenReturn(expected);

        // Act
        String actual = sut.getAdminPassword();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSynchronisationMomentReturnsPropertyReaderGetProperty() {
        // Arrange
        String expected = "MOMENT";
        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(expected);

        // Act
        String actual = sut.getSynchronisationMoment();

        // Assert
        assertEquals(expected, actual);
    }

}
