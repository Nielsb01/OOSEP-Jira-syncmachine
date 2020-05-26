package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserPreferenceDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferenceDataMapper implements IDataMapper<UserPreferenceDTO> {

    @Override
    public UserPreferenceDTO toDTO(ResultSet resultSet) throws SQLException {
        resultSet.next();

        return new UserPreferenceDTO(resultSet.getBoolean("auto_sync"));
    }
}
