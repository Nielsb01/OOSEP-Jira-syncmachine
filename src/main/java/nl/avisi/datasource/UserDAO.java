package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.propertyreaders.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.dto.UserSyncDTO;

import javax.inject.Inject;
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
    private final static boolean AUTO_SYNC_STATUS_VALUE = true;

    /**
     * Column name for the worker of the from
     * JIRA instance
     */
    private final static String JIRA_FROM_WORKER_COLUMN_NAME = "JiraInstantie1Worker";

    /**
     * Column name for the worker of the to
     * JIRA instance
     */
    private final static String JIRA_TO_WORKER_COLUMN_NAME = "Jirainstantie2Worker";

    /**
     * SQL Query to retrieve all users who
     * have chosen to use the auto sync feature
     */
    private final static String GET_ALL_AUTO_SYNC_USERS_SQL = String.format("SELECT %s, %s FROM Jirausers WHERE syncStatus = ?", JIRA_FROM_WORKER_COLUMN_NAME, JIRA_TO_WORKER_COLUMN_NAME);

    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * Inject the database dependency
     *
     * @param database the database
     */
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

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(GET_ALL_AUTO_SYNC_USERS_SQL)) {
            stmt.setBoolean(1, AUTO_SYNC_STATUS_VALUE);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                autoSyncUsers.add(
                        new UserSyncDTO()
                                .setFromWorker(result.getString(JIRA_FROM_WORKER_COLUMN_NAME))
                                .setToWorker(result.getString(JIRA_TO_WORKER_COLUMN_NAME))
                );
            }
        } catch (SQLException e) {
            System.err.printf("Error occurred fetching all users which have enabled auto sync: %s\n", e.getMessage());
        } catch (DatabaseDriverNotFoundException e) {
            System.err.printf("The database driver %s cannot be found on the system\n", e.getMessage());
        }

        return autoSyncUsers;
    }
}
