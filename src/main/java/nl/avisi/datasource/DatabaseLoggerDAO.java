package nl.avisi.datasource;

import nl.avisi.datasource.database.Database;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLoggerDAO {
    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * Table name for logging errors
     */
    private static final String DATABASE_LOGGING_TABLE_NAME = "error";

    /**
     * Column name for className where the error originated from
     */
    private static final String ERROR_ORIGIN_CLASS_COLUMN_NAME = "class_name";

    /**
     * Column name for logging errors
     */
    private static final String ERROR_MESSAGE_COLUMN_NAME = "message";

    /**
     * SQL statement to log the
     * errors to the database
     */
    private static final String LOG_ERROR_SQL = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", DATABASE_LOGGING_TABLE_NAME, ERROR_ORIGIN_CLASS_COLUMN_NAME, ERROR_MESSAGE_COLUMN_NAME);

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Method Logs a given Exeption message to the database with a SQL statement
     *
     * @param errorToLog the message of this exception will be logged in the database
     */
    public void insertLogIntoDatabase(String className, Exception errorToLog) {
        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(LOG_ERROR_SQL)) {
            stmt.setString(1, className);
            stmt.setString(2, errorToLog.getMessage());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(String.format("Error occurred while trying to log to the database %s", e.getMessage()));
        }
    }
}
