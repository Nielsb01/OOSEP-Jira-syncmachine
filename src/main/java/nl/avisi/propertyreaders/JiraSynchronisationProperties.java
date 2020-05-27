package nl.avisi.propertyreaders;

import nl.avisi.propertyreaders.exceptions.InvalidConfigFormatException;

import javax.inject.Inject;

/**
 * Used for querying jiraSyncrhonisation.properties
 */
public class JiraSynchronisationProperties {

    private static final String JIRA_PROPERTIES_FILE_NAME = "jiraSynchronisation.properties";

    private PropertyReader propertyReader;

    @Inject
    public void setPropertyReader(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
        loadPropertyFile();
    }

    /**
     * Loads the given property file so it can be read using the class' other methods. Run this before using any other methods in this class.
     */
    private void loadPropertyFile() {
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

    public String getSynchronisationDayOfWeek() {
        String dayTimeString = getSynchronisationMoment();

        String[] separatedDayTimeStrings = dayTimeString.split("[ :]");

        return convertDayToShortDay(separatedDayTimeStrings[0]);
    }

    public String getSynchronisationHour() {
        String dayTimeString = getSynchronisationMoment();

        String[] separatedDayTimeStrings = dayTimeString.split("[ :]");

        return separatedDayTimeStrings[1];
    }

    public String getSynchronisationMinute() {
        String dayTimeString = getSynchronisationMoment();

        String[] separatedDayTimeStrings = dayTimeString.split("[ :]");

        return separatedDayTimeStrings[2];
    }

    private String getSynchronisationMoment() {
        String dayTimeString = propertyReader.getProperty("synchronisationMoment");

        if (!dayTimeString.matches("[A-Za-z]* [0-9]+:[0-9]+")) {
            throw new InvalidConfigFormatException();
        }

        return dayTimeString;
    }

    private String convertDayToShortDay(String day) {
        String shortDay;

        switch (day) {
            case "Zondag":
                shortDay = "Sun";
                break;
            case "Maandag":
                shortDay = "Mon";
                break;
            case "Dinsdag":
                shortDay = "Tue";
                break;
            case "Woensdag":
                shortDay = "Wed";
                break;
            case "Donderdag":
                shortDay = "Thu";
                break;
            case "Vrijdag":
                shortDay = "Fri";
                break;
            case "Zaterdag":
            default:
                shortDay = "Sat";
                break;
        }

        return shortDay;
    }
}
