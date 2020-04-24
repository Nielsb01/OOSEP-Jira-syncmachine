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


@Path("send")
public class TimeWriteResource {

    //hier verdere implementatie van ophalen en verwerken post front-end
    @GET
    public void sync(){
        //mock data moet van post komen
        String basicAuthUserName = "Nielsb01";
        String basicAuthPass = "OOSEGENUA";

//        String worker = "JIRAUSER10100"; // niels a
        String worker = "JIRAUSER10000"; // niels borkes
        int spendTimeSeconds = 3960;
        String issueKey = "KNBPU-1";

        addWorklog(worker, spendTimeSeconds, issueKey, basicAuthUserName, basicAuthPass);
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
    public void addWorklog(String workerID, int timeSpend, String issueKey, String username, String password) {
//        String urlBase = "http://127.0.0.1/rest/api/2/issue/";
//        String urlEnd = "/worklog";
//        String url = urlBase + issueKey + urlEnd;
        String url = "http://127.0.0.1/rest/tempo-timesheets/4/worklogs";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);
        System.out.println(currentDate);

        WorklogDTO worklogDTO = new WorklogDTO();
        worklogDTO.setWorker(workerID);
        //worklogDTO.setComment("aaa");
        worklogDTO.setStarted(currentDate);
        worklogDTO.setTimeSpentSeconds(timeSpend);
        worklogDTO.setOriginTaskId(issueKey);


        HttpResponse<JsonNode> response = Unirest.post(url)
                .basicAuth(username, password)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(worklogDTO)
                .asJson();

        System.out.println(response.getBody());
    }

}
