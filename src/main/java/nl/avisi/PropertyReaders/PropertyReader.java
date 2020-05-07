package nl.avisi.PropertyReaders;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    private Properties properties;

    public PropertyReader(String propertiesFileName) {
        properties = new Properties();

        try {
            properties.load(Objects
                    .requireNonNull(getClass()
                            .getClassLoader()
                            .getResourceAsStream(propertiesFileName)));
        }
        catch (IOException e) {
            throw new InternalServerErrorException("Error loading Jira synchronisation properties: ", e);
        }
    }

    public String getProperty(String field) {
        return properties.getProperty(field);
    }
}
