package nl.avisi.controller;


import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.WorklogSynchronisation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("sync")
public class SyncController {

    private WorklogSynchronisation worklogSynchronisation;

    @Inject
    public void setWorklogSynchronisation(WorklogSynchronisation worklogSynchronisation) {
        this.worklogSynchronisation = worklogSynchronisation;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response synchroniseWorklogsFromClientToAvisi(WorklogRequestDTO worklogRequestDTO) {

        worklogSynchronisation.createWorklogsInAvisiServer(worklogSynchronisation.retrieveWorklogsFromClientServer(worklogRequestDTO));
        return Response.status(200).entity("Synchronisatie succesvol").build();
    }
}
