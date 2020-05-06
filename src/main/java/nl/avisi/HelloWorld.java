package nl.avisi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("test")
public class HelloWorld {
    RetrieveData retrieveData = new RetrieveData();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorklogDTO> helloWorld() {

        List<String> workers = new ArrayList<>();
        workers.add("JIRAUSER10000");
        workers.add("JIRAUSER10100");

        return retrieveData.retrieveWorklogs("2020-04-01", "2020-04-23", workers);
    }
}
