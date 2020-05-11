package nl.avisi.datasource.contracts;

import nl.avisi.model.UserSyncDTO;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    List<UserSyncDTO> getAllAutoSyncUsers() throws SQLException;
}
