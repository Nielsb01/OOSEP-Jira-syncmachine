package nl.avisi.dto;

public class UserPreferenceDTO {

    /**
     * The user worklogs will be synchronised
     * automatically
     */
    private boolean autoSyncOn;

    public UserPreferenceDTO(boolean autoSyncOn) {
        this.autoSyncOn = autoSyncOn;
    }

    public boolean getAutoSyncOn() {
        return autoSyncOn;
    }

    public void setAutoSyncOn(boolean autoSyncOn) {
        this.autoSyncOn = autoSyncOn;
    }
}
