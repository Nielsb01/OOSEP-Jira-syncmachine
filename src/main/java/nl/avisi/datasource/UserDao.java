package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IUserDao;
import nl.avisi.propertyReaders.exceptions.DatabaseDriverNotFoundException;
import nl.avisi.model.UserSyncDTO;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao to manage the various user objects
 * in the database
 */
public class UserDao implements IUserDao {

    /**
     * Value of the syncStatus field when
     * the user has chosen to use the auto
     * sync feature
     */
    private static String autoSyncStatusValue = "auto";

    /**
     * Column name for the worker of the from
     * JIRA instance
     */
    private static String jiraFromWorkerColumnName = "JiraInstantie1Worker";

    /**
     * Column name for the worker of the to
     * JIRA instance
     */
    private static String jiraToWorkerColumnName = "Jirainstantie2Worker";

    /**
     * SQL Query to retrieve all users who
     * have chosen to use the auto sync feature
     */
    private static String getAllAutoSyncUsersSql = String.format("SELECT %s, %s FROM Jirausers WHERE syncStatus = ?", jiraFromWorkerColumnName, jiraToWorkerColumnName);

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
     * @return all JIRA users with auto syc
     * @throws SQLException when the connection to the database cannot be closed
     */
    public List<UserSyncDTO> getAllAutoSyncUsers() throws SQLException {
        List<UserSyncDTO> autoSyncUsers = new ArrayList<>();

        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = database.connect();
            stmt = connection.prepareStatement(getAllAutoSyncUsersSql);
            stmt.setString(1, autoSyncStatusValue);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                autoSyncUsers.add(
                        new UserSyncDTO()
                                .setFromWorker(result.getString(jiraFromWorkerColumnName))
                                .setToWorker(result.getString(jiraToWorkerColumnName))
                );
            }

        } catch(SQLException e) {
            System.err.printf("Error occurred fetching all users which have enabled auto sync: %s\n", e.getMessage());
        } catch(DatabaseDriverNotFoundException e) {
            System.err.printf("The database driver %s cannot be found on the system\n", e.getMessage());
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return autoSyncUsers;
    }
}
