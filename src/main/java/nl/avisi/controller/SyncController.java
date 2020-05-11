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

@Path("sync")
public class SyncController {

    private JiraWorklog jiraWorklog;

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    /**
     *
     * @param worklogRequestDTO Contains the necessary information needed to retrieve worklogs from the server.
     * @return HTTP response with corresponding status code and entity to the request that was made.
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response synchroniseWorklogsFromClientToAvisi(WorklogRequestDTO worklogRequestDTO) {

        jiraWorklog.createWorklogsOnAvisiServer(jiraWorklog.retrieveWorklogsFromClientServer(worklogRequestDTO));
        return Response.status(200).entity("Synchronisatie succesvol").build();
    }
}