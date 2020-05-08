package nl.avisi;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("test")
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
