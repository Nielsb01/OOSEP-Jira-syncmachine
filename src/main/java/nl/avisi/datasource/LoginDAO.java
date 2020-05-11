package nl.avisi.datasource;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.datasource.datamappers.DataMapper;
import nl.avisi.dto.UserDTO;
import nl.avisi.propertyreaders.exceptions.DatabaseDriverNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Responsible for anything that has to do with the users login
 * information.
 */
public class LoginDAO implements ILoginDAO {

    /**
     * DataMapper is used to convert
     * a resultset to a DTO
     */
    private DataMapper<UserDTO> loginDataMapper;

    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * SQL query for retrieving the users
     * login information based on the supplied username
     */
    private final static String GET_LOGIN_DATA_SQL = "SELECT * FROM sync_machine_accounts WHERE username = ?";

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Inject
    public void setLoginDataMapper(DataMapper<UserDTO> loginDataMapper) {
        this.loginDataMapper = loginDataMapper;
    }

    /**
     * Retrieves all login information corresponding to
     * the supplied username.
     *
     * @param username Supplied by the user. Used for retrieving all the
     *                 corresponding information like password and UserID.
     * @return UserDTO containing all the retrieved information like username, password and UserID.
     */
    @Override
    public UserDTO getLoginInfo(String username) {

        UserDTO userDTO = new UserDTO();

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(GET_LOGIN_DATA_SQL)) {
            stmt.setString(1, username);

           userDTO = loginDataMapper.toDTO(stmt.executeQuery());
        } catch (SQLException e) {
            System.err.printf("Error occurred fetching all users which have enabled auto sync: %s\n", e.getMessage());
        } catch (DatabaseDriverNotFoundException e) {
            System.err.printf("The database driver %s cannot be found on the system\n", e.getMessage());
        }

        return userDTO;
    }
}
