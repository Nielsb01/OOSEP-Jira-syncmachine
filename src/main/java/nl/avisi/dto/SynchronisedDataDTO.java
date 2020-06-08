package nl.avisi.dto;

/**
 * Holds the amount of seconds and amount of worklogs
 * that were successfully and non successfully synchronised
 * during a manual synchronisation
 */
public class SynchronisedDataDTO {

    /**
     * Amount of seconds that were successfully synchronised
     */
    private int totalSynchronisedSeconds;

    /**
     * Amount of seconds that were failed to be synchronised
     */
    private int totalFailedSynchronisedSeconds;

    /**
     * Amount of worklogs that were successfully synchronised
     */
    private int totalSynchronisedWorklogs;

    /**
     * Amount of worklogs that were failed to be synchronised
     */
    private int totalFailedSynchronisedWorklogs;

    public SynchronisedDataDTO(int totalSynchronisedSeconds, int totalFailedSynchronisedSeconds, int totalSynchronisedWorklogs, int totalFailedSynchronisedWorklogs) {
        this.totalSynchronisedSeconds = totalSynchronisedSeconds;
        this.totalFailedSynchronisedSeconds = totalFailedSynchronisedSeconds;
        this.totalSynchronisedWorklogs = totalSynchronisedWorklogs;
        this.totalFailedSynchronisedWorklogs = totalFailedSynchronisedWorklogs;
    }

    public SynchronisedDataDTO() {
    }

    public int getTotalSynchronisedWorklogs() {
        return totalSynchronisedWorklogs;
    }

    public void setTotalSynchronisedWorklogs(int totalSynchronisedWorklogs) {
        this.totalSynchronisedWorklogs = totalSynchronisedWorklogs;
    }

    public int getTotalFailedSynchronisedWorklogs() {
        return totalFailedSynchronisedWorklogs;
    }

    public void setTotalFailedSynchronisedWorklogs(int totalFailedSynchronisedWorklogs) {
        this.totalFailedSynchronisedWorklogs = totalFailedSynchronisedWorklogs;
    }

    public int getTotalSynchronisedSeconds() {
        return totalSynchronisedSeconds;
    }

    public void setTotalSynchronisedSeconds(int totalSynchronisedSeconds) {
        this.totalSynchronisedSeconds = totalSynchronisedSeconds;
    }

    public int getTotalFailedSynchronisedSeconds() {
        return totalFailedSynchronisedSeconds;
    }

    public void setTotalFailedSynchronisedSeconds(int totalFailedSynchronisedSeconds) {
        this.totalFailedSynchronisedSeconds = totalFailedSynchronisedSeconds;
    }
}
