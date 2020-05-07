package nl.avisi;

public class WorklogDTO {
    /**
     * is the id of a user the worklog will be created for. "JIRAUSER10000"
     */
    private String worker;
    /**
     * is a message displayed in the worklog so that is is clear there has been made use of the synchronisation app.
     */
    private String comment = "Logging from JavaSyncApp";
    /**
     * is the date the worklog was created in Jira-server 1.
     */
    private String started;
    /**
     * is the amount of worked time displayed.
     */
    private int timeSpentSeconds;
    /**
     * this id from a Tempo account and will be used to link the issue of Jira-server 1 to the correct issue on Avisi-Jira-server.
     */
    private String originTaskId;

    public String getWorker() {
        return worker;
    }


    public WorklogDTO setWorker(String worker) {
        this.worker = worker;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public WorklogDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getStarted() {
        return started;
    }

    public WorklogDTO setStarted(String started) {
        this.started = started;
        return this;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public WorklogDTO setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
        return this;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public WorklogDTO setOriginTaskId(String originTaskId) {
        this.originTaskId = originTaskId;
        return this;
    }
}
