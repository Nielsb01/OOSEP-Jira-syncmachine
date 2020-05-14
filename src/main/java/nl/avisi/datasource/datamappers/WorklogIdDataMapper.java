package nl.avisi.datasource.datamappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps incoming ResultSet that has to do with worklogs
 * to the correct in-application format
 */
public class WorklogIdDataMapper implements IDataMapper {

    /**
     * Maps the Resultset containing all the worklogIds in the database
     * to a List<Integer>
     *
     * @param resultSet From the database containing all the worklogIds
     * @return List of worklogIds
     * @throws SQLException When any of the interactions with the ResultSet
     * go wrong
     */
    @Override
    public List<Integer> toDTO(ResultSet resultSet) throws SQLException {
        List<Integer> worklogIds = new ArrayList<>();

        while (resultSet.next()) {
            worklogIds.add(resultSet.getInt("worklog_id"));
        }

        return worklogIds;
    }
}
