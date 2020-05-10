package nl.avisi.model;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.dto.LoginDTO;
import nl.avisi.dto.UserDTO;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

/**
 * Handles all logic that has to do with
 * logging in.
 */
public class Login {

    /**
     * Resource responsible for communicating
     * with the database
     */
    private ILoginDAO loginDAO;

    @Inject
    public void setLoginDAO(ILoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    /**
     * Validates the login information given by the user. Retrieves the
     * stored password that corresponds with the given username from the
     * database and compares this to the given password.
     *
     * @param loginDTO contains the login information of the user
     *                 needed to verify their identity
     * @return the UserID the corresponds to the supplied login information
     */
    public int validateCredentials(LoginDTO loginDTO) {
        UserDTO userDTO = loginDAO.getLoginInfo(loginDTO.getUsername());

        if (DigestUtils.sha256Hex(loginDTO.getPassword()).equals(userDTO.getPassword())) {
           return userDTO.getUserID();
        } else {
            throw new NotAuthorizedException("Either the username and/or password wasn't correct!");
        }
    }
}
