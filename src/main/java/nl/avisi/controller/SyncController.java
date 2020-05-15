package nl.avisi.controller;


import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.JiraWorklog;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller that is responsible for processing HTTP requests to this API that have to do with
 * synchronising worklogs.
 */

@Path("synchronise")
public class SyncController {

    private JiraWorklog jiraWorklog;

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    /**
     * @param worklogRequestDTO Contains the necessary information needed to retrieve worklogs from the server
     * @param userId Id of the user that made the request
     * @return HTTP response with corresponding status code
     */
    @Path("/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response manualSynchronisation(WorklogRequestDTO worklogRequestDTO, @PathParam("userId") int userId) {
        jiraWorklog.manualSynchronisation(worklogRequestDTO, userId);
        return Response.status(Response.Status.OK).build();
    }
}
