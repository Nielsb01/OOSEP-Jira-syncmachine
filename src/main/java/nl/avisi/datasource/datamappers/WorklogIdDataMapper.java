package nl.avisi.datasource.datamappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorklogIdDataMapper implements IDataMapper {

    @Override
    public List<Integer> toDTO(ResultSet resultSet) throws SQLException {
        List<Integer> worklogIds = new ArrayList<>();

        while (resultSet.next()) {
            worklogIds.add(resultSet.getInt("worklog_id"));
        }

        return worklogIds;
    }
}
