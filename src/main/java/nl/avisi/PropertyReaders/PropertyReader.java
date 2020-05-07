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
