package nl.avisi;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("send")
public class TimeWriteResource {

    private IRequest request;
    private String url;

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    @GET
    public void sync(){

        //mock data moet van post komen
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);

        List<WorklogDTO> worklogs= new ArrayList<>();
        String basicAuthUserName = "Nielsb01";
        String basicAuthPass = "OOSEGENUA";

        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted(currentDate).setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted(currentDate).setTimeSpentSeconds(720).setOriginTaskId("KNBPU-2"));
        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted(currentDate).setTimeSpentSeconds(780).setOriginTaskId("KNBPU-2"));
        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted(currentDate).setTimeSpentSeconds(840).setOriginTaskId("KNBPU-2"));

        url = "http://127.0.0.1/rest/tempo-timesheets/4/worklogs";
        addWorklog(worklogs, basicAuthUserName, basicAuthPass, url);
    }

    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link WorklogDTO}.
     * the standard comment of the {@link WorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from Jira-server 1 .
     * @param username Global admin usernamme for basicAuth.
     * @param password Global admin usernamme for basicAuth.
     */
    public Map addWorklog(List<WorklogDTO> worklogs, String username, String password, String url) {

        request.setAuthentication(new BasicAuth().setUsername(username).setPassword(password));
        Map<WorklogDTO,Integer> responseCodes = new HashMap<>();

        for(WorklogDTO worklog : worklogs){
            HttpResponse<JsonNode> response = request.post(url,worklog);
            responseCodes.put(worklog,response.getStatus());

        }
        for(Map.Entry<WorklogDTO,Integer> item : responseCodes.entrySet()){
            System.out.println(item);
        }
        return responseCodes;
    }

}