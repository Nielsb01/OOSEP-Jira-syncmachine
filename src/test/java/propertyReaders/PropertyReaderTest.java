package propertyReaders;

import nl.avisi.propertyreaders.exceptions.EmptyPropertyException;
import nl.avisi.propertyreaders.exceptions.PropertyFileNotFoundException;
import nl.avisi.propertyreaders.exceptions.PropertyFileNotLoadedException;
import nl.avisi.propertyreaders.PropertyReader;
import nl.avisi.propertyreaders.exceptions.PropertyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PropertyReaderTest {

    final String PROPERTY_FILE_NAME = "test.properties";

    PropertyReader sut;

    @BeforeEach
    void setup() {
        sut = new PropertyReader();
    }

    @Test
    void testGetPropertyReturnsProperty() {
        // Arrange
        sut.loadPropertyFile(PROPERTY_FILE_NAME);

        String testValue = "testOne";
        String expected = "TEST_ONE";

        // Act
        String actual = sut.getProperty(testValue);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetPropertyReturnsPropertyTwoTimes() {
        // Arrange
        sut.loadPropertyFile(PROPERTY_FILE_NAME);

        String testValue1 = "testOne";
        String expected1 = "TEST_ONE";

        String testValue2 = "testTwo";
        String expected2 = "TEST_TWO";

        // Act
        String actual1 = sut.getProperty(testValue1);
        String actual2 = sut.getProperty(testValue2);

        // Assert
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void testGetPropertyThrowsPropertyFileNotLoadedExceptionWithoutPropertyFileLoaded() {
        // Arrange
        String testValue = "testOne";

        // Act & Assert
        assertThrows(PropertyFileNotLoadedException.class, () -> sut.getProperty(testValue));
    }

    @Test
    void testGetPropertyThrowsEmptyPropertyExceptionWhenPropertyIsEmpty() {
        // Arrange
        sut.loadPropertyFile(PROPERTY_FILE_NAME);

        String testValue = "testThree";

        // Act & Assert
        assertThrows(EmptyPropertyException.class, () -> sut.getProperty(testValue));
    }

    @Test
    void testGetPropertyThrowsPropertyNotFoundExceptionIfPropertyDoesNotExist() {
        // Arrange
        sut.loadPropertyFile(PROPERTY_FILE_NAME);

        String testValue = "testNumberThree";

        // Act & Assert
        assertThrows(PropertyNotFoundException.class, () -> sut.getProperty(testValue));
    }

    @Test
    void testLoadPropertyThrowsPropertyFileNotFoundExceptionIfPropertyFileDoesNotExist() {
        // Arrange
        String testValue = "A property file that does not exist";

        // Act & Assert
        assertThrows(PropertyFileNotFoundException.class, () -> sut.loadPropertyFile(testValue));
    }
}
