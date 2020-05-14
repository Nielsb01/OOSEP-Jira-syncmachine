package nl.avisi.propertyreaders;

import javax.inject.Inject;

public class DatabaseProperties {

    /**
     * File which contains the properties for the
     * database
     */
    private static final String PROPERTIES_FILE = "database.properties";

    /**
     * Configuration property which contains the
     * connection string
     */
    private static final String CONNECTION_PROPERTY_NAME = "connection";

    /**
     * Configuration property which contains the
     * driver class name
     */
    private static final String DRIVER_PROPERTY_NAME = "driver";

    /**
     * Class to read the actual properties from
     * the file
     */
    private PropertyReader propertyReader;

    /**
     * Inject the property reader dependency
     *
     * @param propertyReader the property reader
     */
    @Inject
    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
        loadPropertyFile();
    }

    /**
     * Load the properties file into the
     * property reader
     *
     */
    private void loadPropertyFile() {
        propertyReader.loadPropertyFile(PROPERTIES_FILE);
    }

    /**
     * Get the connection string
     *
     * @return the connection string for the database
     */
    public String getConnectionString() {
        return propertyReader.getProperty(CONNECTION_PROPERTY_NAME);
    }

    /**
     * Get the class the database driver requires
     *
     * @return the class name of the database driver
     */
    public String getDriverName() {
        return propertyReader.getProperty(DRIVER_PROPERTY_NAME);
    }
}
