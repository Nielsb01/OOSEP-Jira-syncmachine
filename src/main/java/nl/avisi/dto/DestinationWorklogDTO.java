package nl.avisi.dto;

/**
 * Class contains the necessary information to
 * post a worklog to the destination server
 */
public class DestinationWorklogDTO {

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

    public DestinationWorklogDTO setWorker(String worker) {
        this.worker = worker;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public DestinationWorklogDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getStarted() {
        return started;
    }

    public DestinationWorklogDTO setStarted(String started) {
        this.started = started;
        return this;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public DestinationWorklogDTO setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
        return this;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public DestinationWorklogDTO setOriginTaskId(String originTaskId) {
        this.originTaskId = originTaskId;
        return this;
    }
}