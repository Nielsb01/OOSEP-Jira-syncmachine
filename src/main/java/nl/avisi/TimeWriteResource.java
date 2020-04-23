package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("send")
public class TimeWriteResource {

    //hier verdere implementatie van ophalen en verwerken post front-end
    @GET
    public void sync(){
        //mock data moet van post komen
        String issueKey = "KVNNTES-1";
        String basicAuthUserName = "Nielsb111";
        String basicAuthPass = "OOSEGenua";
        String spendTime = "29m";
        //end of mock

        addWorklog(issueKey, basicAuthUserName, basicAuthPass, spendTime);
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
    public void addWorklog(String issueKey, String username, String password, String timeSpend) {
        String urlBase = "http://127.0.0.1/rest/api/2/issue/";
        String urlEnd = "/worklog";
        String url = urlBase + issueKey + urlEnd;

        WorklogDTO worklogDTO = new WorklogDTO();
        worklogDTO.setTimeSpent(timeSpend);
                worklogDTO.setComment("new unirest");

        HttpResponse<JsonNode> response = Unirest.post(url)
                .basicAuth(username, password)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(worklogDTO)
                .asJson();

        System.out.println(response.getBody());
    }

}
