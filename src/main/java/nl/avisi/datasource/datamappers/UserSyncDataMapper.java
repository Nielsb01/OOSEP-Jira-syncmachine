package nl.avisi.datasource.datamappers;

import nl.avisi.dto.UserSyncDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for converting data
 * that is retrieved from the jira_user
 * table into a in-application object
 */
public class UserSyncDataMapper implements IDataMapper<UserSyncDTO> {

    public static final String ORIGIN_INSTANCE_USER_KEY = "origin_instance_user_key";
    public static final String DESTINATION_INSTANCE_USER_KEY = "destination_instance_user_key";

    /**
     * Converts the resultSet that is passed in to
     * a UserSyncDTO
     *
     * @param resultSet Contains the data that was retrieved from the database
     * @return UserDTO containing the data from the resultSet
     * @throws SQLException When any of the interactions with the resultSet
     * go wrong
     */
    @Override
    public UserSyncDTO toDTO(ResultSet resultSet) throws SQLException {
        resultSet.next();
       return new UserSyncDTO()
                .setOriginWorker(resultSet.getString(ORIGIN_INSTANCE_USER_KEY))
                .setDestinationWorker(resultSet.getString(DESTINATION_INSTANCE_USER_KEY));
    }
}
