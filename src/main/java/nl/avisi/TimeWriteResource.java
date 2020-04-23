package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("send")
public class TimeWriteResource {

    //hier verdere implementatie van ophalen en verwerken post frontend
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


    private void addWorklog(String issueKey, String username, String password, String timeSpend) {
        String urlBase = "http://127.0.0.1/rest/api/2/issue/";
        String urlEnd = "/worklog";
        String url = urlBase + issueKey + urlEnd;

        WorklogDTO worklogDTO = new WorklogDTO();
        worklogDTO.setTimeSpent(timeSpend);
        //worklogDTO.setComment("new Java test other account");

        HttpResponse<JsonNode> response = Unirest.post(url)
                .basicAuth(username, password)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(worklogDTO)
                .asJson();

        System.out.println(response.getBody());
    }

}
