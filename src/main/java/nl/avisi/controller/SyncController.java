package nl.avisi.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nl.avisi.dto.ManualSyncDTO;
import nl.avisi.dto.SynchronisedDataDTO;
import nl.avisi.model.contracts.IJiraWorklog;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller that is responsible for processing HTTP requests to this API that have to do with
 * synchronising worklogs.
 */
@Api("Synchronise the worklogs for a single user")
@Path("synchronise")
public class SyncController {

    private IJiraWorklog jiraWorklog;

    @Inject
    public void setJiraWorklog(IJiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    /**
     * @param manualSyncDTO Contains the necessary information needed to manually sync
     *                      the users data
     * @param userId Id of the user that made the request
     * @return HTTP response with corresponding status code
     */
    @Path("/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Synchronise the worklogs between two specific dates", notes = "A userId is required")
    public Response manualSynchronisation(ManualSyncDTO manualSyncDTO, @PathParam("userId") int userId) {
        SynchronisedDataDTO synchronisedData = jiraWorklog.manualSynchronisation(manualSyncDTO, userId);
        return Response.status(Response.Status.OK).entity(synchronisedData).build();
    }
}
