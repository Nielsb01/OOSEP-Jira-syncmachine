package nl.avisi.model;

import nl.avisi.datasource.contracts.ILoginDAO;
import nl.avisi.dto.LoginDTO;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;

public class Login {

    private ILoginDAO loginDAO;

    @Inject
    public void setLoginDAO(ILoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    //TODO return type is nu nog void tot we weten wat we willen teruggeven
    public void validateCredentials(LoginDTO loginDTO) {
        if (!DigestUtils.sha256Hex(loginDTO.getPassword()).equals(loginDAO.getLoginInfo(loginDTO.getUsername()).getPassword())) {
            throw new NotAuthorizedException("Either the username and/or password wasn't correct!");
        }
    }
}
