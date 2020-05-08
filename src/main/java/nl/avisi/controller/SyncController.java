package nl.avisi.controller;


import nl.avisi.WorklogSynchronisation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("test")
public class SyncController {

    private WorklogSynchronisation worklogSynchronisation;

    @Inject
    public void setWorklogSynchronisation(WorklogSynchronisation worklogSynchronisation) {
        this.worklogSynchronisation = worklogSynchronisation;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void syncWorklogs() {

    }
}
