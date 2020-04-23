package nl.avisi.controller;

import nl.avisi.dto.LoginDto;
import nl.avisi.exception.InvalidCredentialsException;
import nl.avisi.model.User;
import nl.avisi.service.AuthenticationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("auth")
public class AuthenticationController {

    @Inject
    private AuthenticationService authenticationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User authenticateUser(LoginDto loginCredentials) {
        try {
            return authenticationService.login(loginCredentials);
        } catch(InvalidCredentialsException invalidCredentials) {
            return null;
        }
    }
}
