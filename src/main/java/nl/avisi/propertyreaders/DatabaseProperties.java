package nl.avisi.propertyReaders;

import javax.inject.Inject;

public class DatabaseProperties {

    /**
     * File which contains the properties for the
     * database
     */
    private final static String propertiesFile = "database.properties";

    /**
     * Configuration property which contains the
     * connection string
     */
    private final static String connectionPropertyName = "connection";

    /**
     * Configuration property which contains the
     * driver class name
     */
    private final static String driverPropertyName = "driver";

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
    }

    /**
     * Load the properties file into the
     * property reader
     *
     */
    public void loadPropertyFile() {
        propertyReader.loadPropertyFile(propertiesFile);
    }

    /**
     * Get the connection string
     *
     * @return the connection string for the database
     */
    public String getConnectionString() {
        return propertyReader.getProperty(connectionPropertyName);
    }

    /**
     * Get the class the database driver requires
     *
     * @return the class name of the database driver
     */
    public String getDriverName() {
        return propertyReader.getProperty(driverPropertyName);
    }
}
