package nl.avisi.datasource;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.datasource.datamappers.IDataMapper;
import nl.avisi.dto.UserDTO;

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
    private IDataMapper<UserDTO> userDataMapper;

    /**
     * Class to manage the database connection
     */
    private Database database;

    /**
     * SQL query for retrieving the users
     * login information based on the supplied username
     */
    private static final String GET_LOGIN_DATA_SQL = "SELECT * FROM sync_machine_account WHERE username = ?";

    @Inject
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Inject
    public void setUserDataMapper(IDataMapper<UserDTO> userDataMapper) {
        this.userDataMapper = userDataMapper;
    }

    /**
     * Retrieves all login information corresponding to
     * the supplied username.
     *
     * @param username Supplied by the user. Used for retrieving all the
     *                 corresponding information like password and UserID.
     * @return UserDTO containing all the retrieved information like username, password and UserID.
     * @throws InternalServerErrorException when a SQLException is thrown. This will be caught
     *                                      by the jax-rs ExceptionMapper and produce a response with status code 500.
     */
    @Override
    public UserDTO getLoginInfo(String username) {

        UserDTO userDTO;

        try (Connection connection = database.connect(); PreparedStatement stmt = connection.prepareStatement(GET_LOGIN_DATA_SQL)) {
            stmt.setString(1, username);

            userDTO = userDataMapper.toDTO(stmt.executeQuery());
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return userDTO;
    }
}
