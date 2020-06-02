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
     * database url
     */
    private static final String DATABASE_URL_PROPERTY_NAME = "databaseUrl";

    /**
     * Configuration property which contains the
     * driver class name
     */
    private static final String DRIVER_PROPERTY_NAME = "driver";

    /**
     * Configuration property which contains the
     * database username
     */
    private static final String USERNAME_PROPERTY_NAME = "username";

    /**
     * Configuration property which contains the
     * database password
     */
    private static final String PASSWORD_PROPERTY_NAME = "password";

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
     */
    private void loadPropertyFile() {
        propertyReader.loadPropertyFile(PROPERTIES_FILE);
    }

    /**
     * Get the databaseUrl
     *
     * @return the databaseUrl
     */
    public String getDatabaseUrl() {
        return propertyReader.getProperty(DATABASE_URL_PROPERTY_NAME);
    }

    /**
     * Get the class the database driver requires
     *
     * @return the class name of the database driver
     */
    public String getDriverName() {
        return propertyReader.getProperty(DRIVER_PROPERTY_NAME);
    }

    /**
     * Get the database username
     *
     * @return the database username
     */
    public String getUsername() {
        return propertyReader.getProperty(USERNAME_PROPERTY_NAME);
    }

    /**
     * Get the database password
     *
     * @return the database password
     */
    public String getPassword() {
        return propertyReader.getProperty(PASSWORD_PROPERTY_NAME);
    }
}
