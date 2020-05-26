package nl.avisi.datasource.contracts;

import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.UserSyncDTO;

import java.util.List;

public interface IUserDAO {
    List<UserSyncDTO> getAllAutoSyncUsers();

    void updateJiraUserKeys(JiraUserKeyDTO jiraUserKeyDTO, int userID);

    void setAutoSyncPreference(int userId, boolean autoSyncOn);

    UserSyncDTO getSyncUser(int userId);

    boolean getUserAutoSyncPreference(int userId);
}
