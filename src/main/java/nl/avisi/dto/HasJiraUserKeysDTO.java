package nl.avisi.dto;

public class HasJiraUserKeysDTO {
    /**
     * The user worklogs will be synchronised
     * automatically
     */
    private boolean hasJiraUserKeys;

    public HasJiraUserKeysDTO(boolean hasJiraUserKeys) {
        this.hasJiraUserKeys = hasJiraUserKeys;
    }

    public boolean getHasJiraUserKeys() {
        return hasJiraUserKeys;
    }

    public void setHasJiraUserKeys(boolean hasJiraUserKeys) {
        this.hasJiraUserKeys = hasJiraUserKeys;
    }
}
