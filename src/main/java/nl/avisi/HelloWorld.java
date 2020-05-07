package nl.avisi;

import nl.avisi.network.authentication.BasicAuth;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("test")
public class HelloWorld {

    private RetrieveData retrieveData;

    @Inject
    public void setRetrieveData(RetrieveData retrieveData) {
        this.retrieveData = retrieveData;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorklogDTO> helloWorld() {

        List<String> workers = new ArrayList<>();
   /*     workers.add("JIRAUSER10000");
        workers.add("JIRAUSER10100");
*/
        retrieveData.setBasicAuth(new BasicAuth().setPassword("xtkWMeAbZcWB6FN").setUsername("ruubz2"));
        retrieveData.setUrl("http://127.0.0.1/");

        return retrieveData.retrieveWorklogs("2020-01-01", "2020-12-23", workers);
    }
}
