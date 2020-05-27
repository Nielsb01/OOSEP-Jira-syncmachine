package nl.avisi.model.contracts;

import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.dto.UserPreferenceDTO;

public interface IJiraUser {
    UserPreferenceDTO getAutoSyncPreference(int userId);
    void setAutoSyncPreference(int userId, boolean autoSyncOn);
    void setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO, int userId);
}
