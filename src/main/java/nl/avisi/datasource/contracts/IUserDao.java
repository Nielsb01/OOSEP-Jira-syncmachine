package nl.avisi.datasource.contracts;

import nl.avisi.model.UserSyncDTO;

import java.sql.SQLException;
import java.util.List;

public interface IUserDao {
    List<UserSyncDTO> getAllAutoSyncUsers() throws SQLException;
}
