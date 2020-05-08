package nl.avisi.controller;


import nl.avisi.LoginDTO;

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

    /**
     * Responsible for processing the login request
     *
     * @param loginDTO contains the login information of the user
     *                 needed to verify their identity
     * @return return value is currently a placeholder and determined at a later date
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        //entity is momenteel nog een placeholder tot we weten wat er daadwerkelijk teruggeven moet worden.
        return Response.status(201).entity("nader te bepalen aanroep naar loginDAO").build();
    }
}
