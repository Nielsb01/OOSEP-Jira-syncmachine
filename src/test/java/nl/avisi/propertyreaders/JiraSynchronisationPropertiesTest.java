package nl.avisi.propertyreaders;

<<<<<<< HEAD:src/test/java/propertyReaders/JiraSynchronisationPropertiesTest.java
import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import nl.avisi.propertyreaders.PropertyReader;
import nl.avisi.propertyreaders.exceptions.InvalidConfigFormatException;
=======
>>>>>>> a7bb7ba6c19d3e9900dd265ee4410391806dd32e:src/test/java/nl/avisi/propertyreaders/JiraSynchronisationPropertiesTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testGetSynchronisationMomentReturnsPropertyReaderGetPropertyCalendar() {
        // Arrange
        String configMoment = "Zondag 12:30";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date expected = calendar.getTime();

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        Calendar actual = sut.getSynchronisationMoment();

        // Assert
        assertEquals(expected, actual.getTime());
    }


    @Test
    void testGetSynchronisationMomentThrowsInvalidConfigFormatExceptionIfSynchronisationMomentIsNotFormattedCorrectly() {
        // Arrange
        String configMoment = "Zondag 2:";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act & Assert
        assertThrows(InvalidConfigFormatException.class, () -> sut.getSynchronisationMoment());
    }
}
