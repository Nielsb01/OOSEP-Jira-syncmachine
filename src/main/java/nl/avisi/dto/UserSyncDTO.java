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
    private String originWorker;

    /**
     * The worker key for the JIRA instance
     * where the logged hours need to be synced
     * to
     */
    private String destinationWorker;

    public String getOriginWorker() {
        return originWorker;
    }

    public UserSyncDTO setOriginWorker(String originWorker) {
        this.originWorker = originWorker;
        return this;
    }

    public String getDestinationWorker() {
        return destinationWorker;
    }

    public UserSyncDTO setDestinationWorker(String destinationWorker) {
        this.destinationWorker = destinationWorker;
        return this;
    }
}
