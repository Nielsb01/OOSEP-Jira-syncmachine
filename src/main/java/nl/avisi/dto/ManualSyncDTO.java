package nl.avisi.dto;

/**
 * holds information for manually
 * synchronising worklogs for the
 * user
 */
public class ManualSyncDTO {
    /**
     * Date from which to retrieve worklogs
     */
    private String fromDate;

    /**
     * Date until to retrieve worklogs
     */
    private String untilDate;


    public ManualSyncDTO() {
    }

    public ManualSyncDTO(String fromDate, String untilDate) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

}
