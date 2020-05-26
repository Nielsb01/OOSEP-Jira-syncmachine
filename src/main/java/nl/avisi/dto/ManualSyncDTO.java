package nl.avisi.dto;

public class ManualSyncDTO {
    private String fromDate;
    private String untilDate;
    private int userId;

    public ManualSyncDTO() {
    }

    public ManualSyncDTO(String fromDate, String untilDate, int userId) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
