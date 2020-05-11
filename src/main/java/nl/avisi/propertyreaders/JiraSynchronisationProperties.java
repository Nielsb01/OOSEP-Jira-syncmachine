package nl.avisi.propertyreaders;

import javax.inject.Inject;

/**
 * Used for querying jiraSyncrhonisation.properties
 */
public class JiraSynchronisationProperties {

    private final String JIRA_PROPERTIES_FILE_NAME = "jiraSynchronisation.properties";

    private PropertyReader propertyReader;

    @Inject
    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    /**
     * Loads the given property file so it can be read using the class' other methods. Run this before using any other methods in this class.
     */
    public void loadPropertyFile() {
        propertyReader.loadPropertyFile(JIRA_PROPERTIES_FILE_NAME);
    }

    public String getOriginUrl() {
        return propertyReader.getProperty("originUrl");
    }

    public String getDestinationUrl() {
        return propertyReader.getProperty("destinationUrl");
    }

    public String getAdminUsername() {
        return propertyReader.getProperty("adminUsername");
    }

    public String getAdminPassword() {
        return propertyReader.getProperty("adminPassword");
    }

    public String getSynchronisationMoment() {
        return propertyReader.getProperty("synchronisationMoment");
    }
}
