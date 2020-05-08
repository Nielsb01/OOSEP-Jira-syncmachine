package nl.avisi.propertyReaders;

import nl.avisi.propertyReaders.exceptions.EmptyPropertyException;
import nl.avisi.propertyReaders.exceptions.PropertyFileNotLoadedException;
import nl.avisi.propertyReaders.exceptions.PropertyNotFoundException;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Responsible for querying property files
 */
public class PropertyReader {

    private Properties properties;

    /**
     * Loads the given property file so it can be read using the class' other methods. Run this before using any other methods in this class.
     * @param propertiesFileName The name of the properties file that is to be read, including the file extension
     */
    public void loadPropertyFile(String propertiesFileName) {
        properties = new Properties();

        try {
            properties.load(Objects
                    .requireNonNull(getClass()
                            .getClassLoader()
                            .getResourceAsStream(propertiesFileName)));
        }
        catch (IOException e) {
            throw new InternalServerErrorException("Error loading properties: ", e);
        }
    }

    /**
     * Returns the value of the given property in the classes property file. Can only be used if loadPropertyFile() has been called earlier.
     * @param field The field of which the value is queried
     * @return The value of the given field in the property file
     */
    public String getProperty(String field) {
        String propertyValue;

        if (properties == null) {
            throw new PropertyFileNotLoadedException();
        }

        propertyValue = properties.getProperty(field);

        if (propertyValue == null) {
            throw new PropertyNotFoundException();
        }

        if (propertyValue.isEmpty()) {
            throw new EmptyPropertyException();
        }

        return propertyValue;
    }
}
