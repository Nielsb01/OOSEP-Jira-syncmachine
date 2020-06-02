
package nl.avisi.controller;


import nl.avisi.dto.LoginDTO;
import nl.avisi.model.contracts.ILogin;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller that is responsible for
 * processing HTTP requests to this API
 * that have to do with logging in.
 */

@Path("/login")
public class LoginController {
    private ILogin login;

    @Inject
    public void setLogin(ILogin login) {
        this.login = login;
    }

    /**
     * Responsible for processing the login request
     *
     * @param loginDTO contains the login information of the user
     *                 needed to verify their identity
     * @return Response containing the UserID corresponding to the
     * supplied login information and the appropriate status code.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        return Response.status(Response.Status.OK).entity(login.validateCredentials(loginDTO)).build();
    }
}
