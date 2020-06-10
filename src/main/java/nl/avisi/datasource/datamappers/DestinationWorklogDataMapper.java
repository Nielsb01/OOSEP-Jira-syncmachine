package nl.avisi.datasource.datamappers;

import nl.avisi.dto.DestinationWorklogDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DestinationWorklogDataMapper implements IDataMapper<DestinationWorklogDTO> {

    /**
     * Converts the resultset that is passed in to
     * a DestinationWorklogDTO
     *
     * @param resultSet Contains the data that was retrieved from the database
     * @return DestinationWorklogDTO containing the data from the resultset
     * @throws SQLException When any of the interactions with the resultset
     * go wrong
     */
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
