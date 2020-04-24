package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Path("send")
public class TimeWriteResource {

    //hier verdere implementatie van ophalen en verwerken post front-end
    @GET
    public void sync(){

        //mock data moet van post komen
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);
        System.out.println(currentDate);

        List<WorklogDTO> worklogs= new ArrayList<>();
        String basicAuthUserName = "Nielsb01";
        String basicAuthPass = "OOSEGENUA";

        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 3960, "KNBPU-1"));
        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 1800, "KNBPU-1"));
        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 1800, "KNBPU-1"));
        worklogs.add(new WorklogDTO("JIRAUSER10100", currentDate, 3600, "KNBPU-1"));

//        String worker = "JIRAUSER10100"; // niels a
//        String worker = "JIRAUSER10000"; // niels borkes


        addWorklog(worklogs, basicAuthUserName, basicAuthPass);
    }

    /***
     * Method creates a new worklog for a user by sending a post request to the Jira API,
     * the location of where the worklog should be created is specified by the issueKey.
     * the standard comment of the worklog will be "Logging from JavaSyncApp"
     *
     * @param issueKey Tag used to specify Jira url.
     * @param username for basicAuth.
     * @param password for basicAuth.
     * @param timeSpend total worked time on issueKey.
     */
    public void addWorklog(List<WorklogDTO> worklogs, String username, String password) {

        String url = "http://127.0.0.1/rest/tempo-timesheets/4/worklogs";

        for(WorklogDTO worklog : worklogs){
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .basicAuth(username, password)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(worklog)
                    .asJson();

            System.out.println(response.getBody());
        }
    }

}
