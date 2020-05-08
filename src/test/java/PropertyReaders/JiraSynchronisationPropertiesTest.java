package PropertyReaders;

import nl.avisi.PropertyReaders.JiraSynchronisationProperties;
import nl.avisi.PropertyReaders.PropertyReader;
import org.junit.Test;

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
        var expected = "com.mysql.cj.jdbc.Driver";

        // Act
        var actual = sut.getDriver();

        // Assert
        assertEquals(expected, actual);
    }

    testGetOriginUrlCallsPropertyReaderGetProperty()
    testGetDestinationUrlCallsPropertyReaderGetProperty()
    testGetAdminUsernameCallsPropertyReaderGetProperty()
    testGetAdminPasswordCallsPropertyReaderGetProperty()
    testGetSynchronisationMomentCallsPropertyReaderGetProperty()
}
