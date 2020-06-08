package nl.avisi;

import nl.avisi.datasource.AutomaticSynchronisationDAO;
import nl.avisi.timer.AutomaticSynchronisationTimer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/fiets")
public class maid {
    @Inject
    AutomaticSynchronisationDAO automaticSynchronisationDAO;


    @GET
    public void boisbois() {
//        automaticSynchronisationTimer.autoSynchronise();
    }
}
