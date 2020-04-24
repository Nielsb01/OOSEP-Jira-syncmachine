package nl.avisi;

public class WorklogDTO {
    private String worker;
    private String comment = "Logging from JavaSyncApp";
    private String started;
    private int timeSpentSeconds;
    private String originTaskId;

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public void setOriginTaskId(String originTaskId) {
        this.originTaskId = originTaskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
