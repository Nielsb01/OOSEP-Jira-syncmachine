package nl.avisi.dto;

/**
 * Object to check which JIRA
 * worker keys are used to sync
 * hours
 */
public class UserSyncDTO {

    /**
     * The worker key from the JIRA instance
     * where the employees log their hours
     */
    private String fromWorker;

    /**
     * The worker key for the JIRA instance
     * where the logged hours need to be synced
     * to
     */
    private String toWorker;

    public String getFromWorker() {
        return fromWorker;
    }

    public UserSyncDTO setFromWorker(String fromWorker) {
        this.fromWorker = fromWorker;
        return this;
    }

    public String getToWorker() {
        return toWorker;
    }

    public UserSyncDTO setToWorker(String toWorker) {
        this.toWorker = toWorker;
        return this;
    }
}
