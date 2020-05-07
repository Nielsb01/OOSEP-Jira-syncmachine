package nl.avisi.PropertyReaders;

import javax.inject.Inject;

public class JiraSynchronisationProperties {

    private PropertyReader propertyReader;

    @Inject
    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    public void loadPropertyFile() {
        propertyReader.loadPropertyFile("jiraSynchronisation.properties");
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
