package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserPreferenceDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferenceDataMapper implements IDataMapper<UserPreferenceDTO> {

    /**
     * Converts the resultset that is passed in to
     * a UserPreferenceDTO
     *
     * @param resultSet Contains the data that was retrieved from the database
     * @return UserPreferenceDTO containing the data from the resultset
     * @throws SQLException When any of the interactions with the resultset
     * go wrong
     */
    @Override
    public UserPreferenceDTO toDTO(ResultSet resultSet) throws SQLException {
        resultSet.next();

        return new UserPreferenceDTO(resultSet.getBoolean("auto_sync"));
    }
}
