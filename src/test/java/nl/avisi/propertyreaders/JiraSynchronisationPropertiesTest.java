package nl.avisi.propertyreaders;

import nl.avisi.propertyreaders.exceptions.InvalidConfigFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;

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
    void testSetPropertyReaderCallsPropertyReaderLoadProperties() {
        // Arrange

        // Act

        // Assert
        Mockito.verify(mockedPropertyReader).loadPropertyFile(anyString());
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
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsSunday() {
        // Arrange
        String configMoment = "Zondag 12:30";

        String expected = "Sun";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsMonday() {
        // Arrange
        String configMoment = "Maandag 12:30";

        String expected = "Mon";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsTuesday() {
        // Arrange
        String configMoment = "Dinsdag 12:30";

        String expected = "Tue";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsWednesday() {
        // Arrange
        String configMoment = "Woensdag 12:30";

        String expected = "Wed";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsThursday() {
        // Arrange
        String configMoment = "Donderdag 12:30";

        String expected = "Thu";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsFriday() {
        // Arrange
        String configMoment = "Vrijdag 12:30";

        String expected = "Fri";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationDayOfWeekReturnsCorrectDayIfDayIsSaturday() {
        // Arrange
        String configMoment = "Zaterdag 12:30";

        String expected = "Sat";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationDayOfWeek();

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    void testGetSynchronisationDayOfWeekThrowsInvalidConfigFormatExceptionIfSynchronisationMomentIsNotFormattedCorrectly() {
        // Arrange
        String configMoment = "Zondag 2:";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act & Assert
        assertThrows(InvalidConfigFormatException.class, () -> sut.getSynchronisationDayOfWeek());
    }

    @Test
    void testGetSynchronisationHourReturnsCorrectHour() {
        // Arrange
        String configMoment = "Zaterdag 12:30";

        String expected = "12";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationHour();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetSynchronisationHourThrowsInvalidConfigFormatExceptionIfSynchronisationMomentIsNotFormattedCorrectly() {
        // Arrange
        String configMoment = "Zondag 2:";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act & Assert
        assertThrows(InvalidConfigFormatException.class, () -> sut.getSynchronisationHour());
    }

    @Test
    void testGetSynchronisationMinuteReturnsCorrectMinute() {
        // Arrange
        String configMoment = "Zaterdag 12:30";

        String expected = "30";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act
        String actual = sut.getSynchronisationMinute();

        // Assert
        assertEquals(expected, actual);

    }

    @Test
    void testGetSynchronisationMinuteThrowsInvalidConfigFormatExceptionIfSynchronisationMomentIsNotFormattedCorrectly() {
        // Arrange
        String configMoment = "Zondag 2:";

        Mockito.when(mockedPropertyReader.getProperty("synchronisationMoment")).thenReturn(configMoment);

        // Act & Assert
        assertThrows(InvalidConfigFormatException.class, () -> sut.getSynchronisationMinute());
    }
}
