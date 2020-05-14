package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Responsible for converting data
 * that is retrieved from the database
 * into a DTO
 */
public class UserDataMapper implements IDataMapper<UserDTO> {

    /**
     * Converts the resultset that is passed in to
     * a UserDTO
     *
     * @param resultSet Contains the data that was retrieved from the database
     * @return UserDTO containing the data from the resultset
     * @throws SQLException When any of the interactions with the resultset
     * go wrong
     */
    @Override
    public UserDTO toDTO(ResultSet resultSet) throws SQLException {
        UserDTO userDTO = new UserDTO();

        resultSet.next();

        userDTO.setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setUserID(resultSet.getInt("user_id"));

        return userDTO;
    }
}