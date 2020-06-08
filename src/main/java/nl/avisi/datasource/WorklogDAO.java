package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.logger.ILogger;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * interacts with the database for anything that has to do with worklogs
 */
public class WorklogDAO implements IWorklogDAO {

    /**
     * responsible for logging errors
     */
    private ILogger logger;

    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * Table name for failed worklog
     */
    private static final String FAILED_WORKLOG_TABLE_NAME = "failed_worklog";

    /**
     * SQL statement for inserting a worlog
     * into the failed_worklog table
     */
    private static final String ADD_FAILED_WORKLOG_SQL = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?)", FAILED_WORKLOG_TABLE_NAME);

    /**
     * SQL query for inserting a new worklogId
     */
    private static final String ADD_WORKLOG_ID_SQL = "INSERT INTO synchronised_worklog (worklog_id) VALUES (?)";

    /**
     * SQL query for retrieving all worklogIds
     */
    private static final String GET_ALL_WORKLOG_IDS_SQL = "SELECT worklog_id FROM synchronised_worklog";

    /**
     * Class to map Resultsets to in-application objects
     */
    private IDataMapper<List<Integer>> worklogIdDataMapper;

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Inject
    public void setWorklogIdDataMapper(IDataMapper<List<Integer>> worklogIdDataMapper) {
        this.worklogIdDataMapper = worklogIdDataMapper;
    }

    @Inject
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    /**
     * Adds a worklogId to the database
     *
     * @param worklogId To be added to the database
     */
    @Override
    public void addWorklogId(int worklogId) {

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(ADD_WORKLOG_ID_SQL)) {
            stmt.setInt(1, worklogId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logToDatabase(getClass().getName(), "addWorklogId", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Adds a worklog to the database with the corresponding id
     * when it has failed to synchronise.
     *
     * @param worklog   The worklog that needs to be added to the database
     * @param worklogId The matching worklogId
     */
    @Override
    public void addFailedworklog(DestinationWorklogDTO worklog, Integer worklogId) {
        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(ADD_FAILED_WORKLOG_SQL)) {
            stmt.setInt(1, worklogId);
            stmt.setString(2, worklog.getWorker());
            stmt.setString(3, worklog.getStarted());
            stmt.setInt(4, worklog.getTimeSpentSeconds());
            stmt.setString(5, worklog.getOriginTaskId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.logToDatabase(getClass().getName(), "addFailedworklog", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Retrieves all worklogIds from the database
     *
     * @return List of all the worklogIds in the database
     */
    @Override
    public List<Integer> getAllWorklogIds() {
        List<Integer> worklogIds;

        try (Connection connection = database.connect();
             PreparedStatement stmt = connection.prepareStatement(GET_ALL_WORKLOG_IDS_SQL)) {
            worklogIds = worklogIdDataMapper.toDTO(stmt.executeQuery());
        } catch (SQLException e) {
            logger.logToDatabase(getClass().getName(), "getAllWorklogIds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        return worklogIds;
    }
}
