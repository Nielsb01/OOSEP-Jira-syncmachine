package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.datasource.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.dto.UserSyncDTO;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao to manage the various user objects
 * in the database
 */
public class UserDAO implements IUserDAO {

    /**
     * Value of the syncStatus field when
     * the user has chosen to use the auto
     * sync feature
     */
    private static final boolean AUTO_SYNC_STATUS_VALUE = true;

    /**
     * Column name for the worker of the from
     * JIRA instance
     */
    private static final String JIRA_ORIGIN_WORKER_COLUMN_NAME = "origin_instance_user_key";

    /**
     * Column name for the worker of the to
     * JIRA instance
     */
    private static final String JIRA_DESTINATION_WORKER_COLUMN_NAME = "destination_instance_user_key";

    /**
     * SQL Query to retrieve all users who
     * have chosen to use the auto sync feature
     */
    private static final String GET_ALL_AUTO_SYNC_USERS_SQL = String.format("SELECT %s, %s FROM Jirausers WHERE syncStatus = ?", JIRA_ORIGIN_WORKER_COLUMN_NAME, JIRA_DESTINATION_WORKER_COLUMN_NAME);

    /**
     * SQL statement to update the
     * jira user keys for a user
     */
    private final static String UPDATE_JIRA_USER_KEY_SQL = String.format("UPDATE jira_user SET %s = ?, %s = ? WHERE user_id = ?", JIRA_ORIGIN_WORKER_COLUMN_NAME, JIRA_DESTINATION_WORKER_COLUMN_NAME);

    /**
     * SQL statement to update the
     * auto synchronisation preference
     * for a user
     */
    private static final String UPDATE_AUTO_SYNC_PREFERENCE_SQL = "UPDATE jira_user SET auto_sync = ? WHERE user_id = ?";

    /**
     * SQL query to retrieve
     * user keys for the given
     * user id
     */
    private static final String GET_SYNC_USER_SQL = String.format("SELECT %s, %s FROM jira_user WHERE user_id = ?", JIRA_ORIGIN_WORKER_COLUMN_NAME, JIRA_DESTINATION_WORKER_COLUMN_NAME);

    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * Maps resultSet to in-application object
     */
    private IDataMapper<UserSyncDTO> userSyncDataMapper;

    @Inject
    public void setUserSyncDataMapper(IDataMapper<UserSyncDTO> userSyncDataMapper) {
        this.userSyncDataMapper = userSyncDataMapper;
    }

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Get all JIRA users which have auto sync enabled
     *
     * @return all JIRA users with auto sync
     */
    public List<UserSyncDTO> getAllAutoSyncUsers() {
        List<UserSyncDTO> autoSyncUsers = new ArrayList<>();

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_AUTO_SYNC_USERS_SQL)) {
            stmt.setBoolean(1, AUTO_SYNC_STATUS_VALUE);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                autoSyncUsers.add(userSyncDataMapper.toDTO(result));
            }

        } catch (SQLException e) {
            System.err.printf("Error occurred fetching all users which have enabled auto sync: %s\n", e.getMessage());
        } catch (DatabaseDriverNotFoundException e) {
            System.err.printf("The database driver %s cannot be found on the system\n", e.getMessage());
        }

        return autoSyncUsers;
    }

    /**
     * Updates the auto sync preference in the database for the given user id
     *
     * @param userId     Id of the user that wants to update their preference
     * @param autoSyncOn Boolean value containing the status of the users
     *                   auto sync preference
     */
    @Override
    public void setAutoSyncPreference(int userId, boolean autoSyncOn) {

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_AUTO_SYNC_PREFERENCE_SQL)) {
            stmt.setBoolean(1, autoSyncOn);
            stmt.setInt(2, userId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(String.format("Error occurred while updating the auto synchronisation status: %s", e.getMessage()));
        }

    }

    /**
     * Gets the user keys of the origin and destination instance
     * from the database that match the user id
     *
     * @param userId Id of the user that made the request
     * @return UserSyncDTO containing the user keys
     */
    @Override
    public UserSyncDTO getSyncUser(int userId) {
        UserSyncDTO userSyncDTO;

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(GET_SYNC_USER_SQL)) {

            stmt.setInt(1, userId);
            userSyncDTO = userSyncDataMapper.toDTO(stmt.executeQuery());

        } catch (SQLException e) {
            throw new InternalServerErrorException(String.format("Error occurred while retrieving a synchronisation user: %s", e.getMessage()));
        }
        return userSyncDTO;
    }

    /**
     * Updates the Jira user keys that match the
     * given user id
     *
     * @param jiraUserKeyDTO Contain the origin and destination user keys
     * @param userId Id of the user that made the request
     */
    @Override
    public void updateJiraUserKeys(JiraUserKeyDTO jiraUserKeyDTO, int userId) {
            try (Connection connection = database.connect();
                 PreparedStatement stmt = connection.prepareStatement(UPDATE_JIRA_USER_KEY_SQL)) {
                stmt.setString(1, jiraUserKeyDTO.getOriginUserKey());
                stmt.setString(2, jiraUserKeyDTO.getDestinationUserKey());
                stmt.setInt(3, userId);

                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new InternalServerErrorException(String.format("Error occurred while retrieving a synchronisation user: %s", e.getMessage()));
            }
        }
}
