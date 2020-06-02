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

    public UserSyncDTO() {
    }

    public UserSyncDTO(String originWorker, String destinationWorker) {
        this.originWorker = originWorker;
        this.destinationWorker = destinationWorker;
    }

    public String getOriginWorker() {
        return originWorker;
    }

    public void setOriginWorker(String originWorker) {
        this.originWorker = originWorker;
    }

    public String getDestinationWorker() {
        return destinationWorker;
    }

    public void setDestinationWorker(String destinationWorker) {
        this.destinationWorker = destinationWorker;
    }
}
