package nl.avisi.PropertyReaders;

public class JiraSynchronisationProperties {

    private PropertyReader propertyReader;

    public JiraSynchronisationProperties() {
        this.propertyReader = new PropertyReader("jiraSynchronisation.properties");
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
