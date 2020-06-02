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

    public DestinationWorklogDTO() {
    }

    public DestinationWorklogDTO(String worker, String started, int timeSpentSeconds, String originTaskId) {
        this.worker = worker;
        this.started = started;
        this.timeSpentSeconds = timeSpentSeconds;
        this.originTaskId = originTaskId;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
