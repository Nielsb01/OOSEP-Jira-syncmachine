package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataMapper implements DataMapper<UserDTO> {
    @Override
    public UserDTO toDTO(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO();

        while (resultSet.next()) {
            userDTO.setUsername(resultSet.getString("Username"))
                    .setPassword(resultSet.getString("Password"))
                    .setUserID(resultSet.getInt("UserID"));

        }
        return userDTO ;
    }
}
