package nl.avisi.propertyreaders;

import nl.avisi.propertyreaders.exceptions.InvalidConfigFormatException;

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

    public Calendar getSynchronisationMoment() {
        return convertStringToCalendar(propertyReader.getProperty("synchronisationMoment"));
    }

    private Calendar convertStringToCalendar(String dayTimeString) {

        if (!dayTimeString.matches("[A-Za-z]* [0-9]+:[0-9]+:[0-9]+")) {
            throw new InvalidConfigFormatException();
        }

        String[] separatedDayTimeStrings = dayTimeString.split("[ :]");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, convertDayToCalendarDay(separatedDayTimeStrings[0]));
        calendar.set(Calendar.HOUR, Integer.parseInt(separatedDayTimeStrings[1]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(separatedDayTimeStrings[2]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private int convertDayToCalendarDay(String day) {
        int numeratedDay;

        switch (day) {
            case "Zondag":
                numeratedDay = Calendar.SUNDAY;
                break;
            case "Maandag":
                numeratedDay = Calendar.MONDAY;
                break;
            case "Dinsdag":
                numeratedDay = Calendar.TUESDAY;
                break;
            case "Woensdag":
                numeratedDay = Calendar.WEDNESDAY;
                break;
            case "Donderdag":
                numeratedDay = Calendar.THURSDAY;
                break;
            case "Vrijdag":
                numeratedDay = Calendar.FRIDAY;
                break;
            case "Zaterdag":
                numeratedDay = Calendar.SATURDAY;
                break;
            default:
                numeratedDay = Calendar.SUNDAY;
                break;
        }

        return numeratedDay;
    }
}
