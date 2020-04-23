package nl.avisi;

public class WorklogDTO {
    private String timeSpent;
    private String comment = "Logging from JavaSyncApp";

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
