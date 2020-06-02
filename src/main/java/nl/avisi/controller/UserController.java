
package nl.avisi.controller;

import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.model.contracts.IJiraUser;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Responsible for dealing with all incoming
 * HTTP requests that are related to the user
 */

@Path("user")
public class UserController {
    private IJiraUser jiraUser;

    @Inject
    public void setJiraUser(IJiraUser jiraUser) {
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
     * @param userId Id of the user that is making the request.
     * @return A HTTP response with the appropriate response code.
     */
    @Path("/jiraUserKey/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO, @PathParam("userId") int userId) {
        jiraUser.setJiraUserKeys(jiraUsernameDTO, userId);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Responsible for dealing with the HTTP request
     * to change the auto synchronisation preference
     * of the user
     *
     * @param userId Id of the user that is making the request
     * @param autoSyncOn Contains boolean value which indicates whether or not
     *                   the auto sync should be enabled or disabled
     * @return A HTTP response with the appropriate response code
     */
    @Path("/autoSync/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setAutoSyncPreference(@PathParam("userId") int userId, @QueryParam("autoSync") boolean autoSyncOn) {
        jiraUser.setAutoSyncPreference(userId, autoSyncOn);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Responsible for retrieving the preferences for
     * the user
     *
     * @param userId Id of the user that is requesting it's preferences
     * @return A HTTP response with the preferences JSON object
     */
    @Path("/autoSync/{userId}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAutoSyncPreference(@PathParam("userId") int userId) {
        return Response.status(Response.Status.OK).entity(jiraUser.getAutoSyncPreference(userId)).build();
    }
}
