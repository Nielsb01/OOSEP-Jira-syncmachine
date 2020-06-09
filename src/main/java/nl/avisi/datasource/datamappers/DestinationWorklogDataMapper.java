package nl.avisi.datasource.datamappers;

import nl.avisi.dto.DestinationWorklogDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DestinationWorklogDataMapper implements IDataMapper<DestinationWorklogDTO> {

    @Override
    public DestinationWorklogDTO toDTO(ResultSet resultSet) throws SQLException {
        return new DestinationWorklogDTO(
                resultSet.getString("worker"),
                resultSet.getString("started"),
                resultSet.getInt("time_spent_seconds"),
                resultSet.getString("origin_task_id")
                );
    }
}
