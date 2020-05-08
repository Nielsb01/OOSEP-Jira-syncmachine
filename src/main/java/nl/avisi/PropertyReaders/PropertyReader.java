package nl.avisi.PropertyReaders;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    private Properties properties;

    public PropertyReader() {
        properties = new Properties();
    }

    /**
     * Loads the given property file so it can be read using the class' other methods. Run this before using any other methods in this class.
     * @param propertiesFileName The name of the properties file that is to be read, including the file extension
     */
    public void loadPropertyFile(String propertiesFileName) {
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

    public String getProperty(String field) {
        return properties.getProperty(field);
    }
}
