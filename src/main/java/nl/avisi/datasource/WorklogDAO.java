package nl.avisi.datasource;

import nl.avisi.datasource.contracts.IWorklogDAO;
import nl.avisi.datasource.datamappers.IDataMapper;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WorklogDAO implements IWorklogDAO {

    private Database database;

    private final static String ADD_WORKLOG_ID_SQL = "INSERT INTO worklog (worklog_id) VALUES (?)";

    private final static String GET_ALL_WORKLOG_IDS_SQL = "SELECT * FROM worklog";

    private IDataMapper<List<Integer>> worklogIdDataMapper;

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Inject
    public void setWorklogIdDataMapper(IDataMapper<List<Integer>> worklogIdDataMapper) {
        this.worklogIdDataMapper = worklogIdDataMapper;
    }

    @Override
    public void addWorklogId(int worklogId) {

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(ADD_WORKLOG_ID_SQL)) {
            stmt.setInt(1, worklogId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public List<Integer> getAllWorklogIds() {
        List<Integer> worklogIds;

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(GET_ALL_WORKLOG_IDS_SQL)) {
            worklogIds = worklogIdDataMapper.toDTO(stmt.executeQuery());
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return worklogIds;
    }
}