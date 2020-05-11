
package nl.avisi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Responsible for dealing with all incoming
 * HTTP requests that are related to the user
 */

@Path("user")
public class UserController {
    private JiraUser jiraUser;

    @Inject
    public void setJiraUser(JiraUser jiraUser) {
        this.jiraUser = jiraUser;
    }

    /**
     * Responsible for dealing with the
     * HTTP request to set the jira user
     * keys that correspond with the
     * supplied Jira usernames.
     *
     * @param jiraUsernameDTO Contain the usernames given by the user for both the Avisi
     *                        and client instance of the Jira server.
     * @return A HTTP response with the appropriate response code.
     */
    @Path("/userKey")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO) {
        jiraUser.setJiraUserKeys(jiraUsernameDTO);
        return Response.status(200).build();
    }
}