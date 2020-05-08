package nl.avisi.controller;


import nl.avisi.model.RetrieveData;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("sync")
public class SyncController {

    private RetrieveData retrieveData;

    @Inject
    public void setRetrieveData(RetrieveData retrieveData) {
        this.retrieveData = retrieveData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void syncWorklogs() {

    }
}
