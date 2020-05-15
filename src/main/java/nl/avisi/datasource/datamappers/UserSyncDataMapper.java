package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserSyncDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserSyncDataMapper implements IDataMapper {

    @Override
    public UserSyncDTO toDTO(ResultSet resultSet) throws SQLException {
        return null;
    }

    public List<UserSyncDTO> toList(ResultSet resultSet) throws SQLException {

    }
}
