package nl.avisi.propertyReaders;

import javax.inject.Inject;
import java.util.Calendar;

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

    private Calendar convertStringToCalendar(String dateTimeString) {
        final char SEPARATOR = ':';

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR, extractIntFromString(dateTimeString, SEPARATOR, 1));
        calendar.set(Calendar.MINUTE, extractIntFromString(dateTimeString, SEPARATOR, 2));
        calendar.set(Calendar.SECOND, extractIntFromString(dateTimeString, SEPARATOR, 3));
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private int extractCalendarDayFromString(String dateTimeString) {
        int weekday;

        switch (dateTimeString) {
            case "Zondag":
                weekday = Calendar.SUNDAY;
                break;
            case "Maandag":
                weekday = Calendar.MONDAY;
                break;
            case "Dinsdag":
                weekday = Calendar.TUESDAY;
                break;
            case "Woensdag":
                weekday = Calendar.WEDNESDAY;
                break;
            case "Donderdag":
                weekday = Calendar.THURSDAY;
                break;
            case "Vrijdag":
                weekday = Calendar.FRIDAY;
                break;
            case "Zaterdag":
                weekday = Calendar.SATURDAY;
                break;
            default:

        }

        return weekday;
    }

    private int extractIntFromString(String dateTimeString, char separator, int position) {

        return int;
        Zondag 00:00:00
    }
}
