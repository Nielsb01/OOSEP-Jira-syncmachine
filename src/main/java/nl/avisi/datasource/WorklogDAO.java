package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.datasource.database.Database;
import nl.avisi.datasource.datamappers.IDataMapper;

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
     * Class to manage the database connection
     */
    private Database database;

    /**
     * SQL query for inserting a new worklogId
     */
    private static final String ADD_WORKLOG_ID_SQL = "INSERT INTO worklog (worklog_id) VALUES (?)";

    /**
     * SQL query for retrieving all worklogIds
     */
    private static final String GET_ALL_WORKLOG_IDS_SQL = "SELECT worklog_id FROM worklog";

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
            throw new InternalServerErrorException(e.getMessage());
        }

        return worklogIds;
    }
}
