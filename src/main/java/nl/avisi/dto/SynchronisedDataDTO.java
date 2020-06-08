package nl.avisi.dto;

/**
 * Holds the amount of seconds that were successfully
 * and non successfully synchronised during a manual synchronisation
 */
public class SynchronisedTimeDTO {
    /**
     * Amount of seconds that were successfully synchronised
     */
    private int totalSynchronisedSeconds;

    /**
     * Amount of seconds that were failed to be synchronised
     */
    private int totalFailedSynchronisedSeconds;

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
