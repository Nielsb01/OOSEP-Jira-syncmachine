package nl.avisi.controller;

import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.model.JiraUser;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserController {
    private JiraUser jiraUser;

    @Inject
    public void setJiraUser(JiraUser jiraUser) {
        this.jiraUser = jiraUser;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO) {
        return Response.status(201).entity("succes").build();
    }
}
