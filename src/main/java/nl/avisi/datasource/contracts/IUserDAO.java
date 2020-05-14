package nl.avisi.datasource.contracts;

import nl.avisi.dto.UserSyncDTO;

import java.util.List;

public interface IUserDAO {
    List<UserSyncDTO> getAllAutoSyncUsers();

    void setAutoSyncPreference(int userId, boolean autoSyncOn);
}
