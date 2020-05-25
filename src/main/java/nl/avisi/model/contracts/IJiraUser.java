package nl.avisi.model.contracts;

import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;

public interface IJiraUser {
    JiraUserKeyDTO retrieveJiraUserKeyByUsername(JiraUsernameDTO jiraUsernameDTO);
    void setAutoSyncPreference(int userId, boolean autoSyncOn);
    void setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO, int userId);
}
