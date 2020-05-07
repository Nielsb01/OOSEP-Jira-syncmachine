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
        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted(currentDate).setTimeSpentSeconds(780).setOriginTaskId("KNBPU-4"));
        worklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted(currentDate).setTimeSpentSeconds(840).setOriginTaskId("KNBPU-2"));

        String url = "http://127.0.0.1/rest/tempo-timesheets/4/worklogs";
        addWorklog(worklogs, basicAuthUserName, basicAuthPass, url);
    }

    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link WorklogDTO}.
     * the standard comment of the {@link WorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from Jira-server 1 .
     * @param username for basicAuth.
     * @param password for basicAuth.
     */
    public void addWorklog(List<WorklogDTO> worklogs, String username, String password, String url) {

        request.setAuthentication(new BasicAuth().setUsername(username).setPassword(password));
        Map<WorklogDTO,Integer> responseCodes = new HashMap<>();

        for(WorklogDTO worklog : worklogs){
            HttpResponse<JsonNode> response = request.post(url,worklog);
            responseCodes.put(worklog,response.getStatus());
            System.out.println(response.getBody());

        }
        for(Map.Entry item : responseCodes.entrySet()){
            System.out.println(item);
        }
    }

}

//@Path("send")
//public class TimeWriteResource {
//
//    //hier verdere implementatie van ophalen en verwerken post front-end
//    @GET
//    public void sync(){
//
//        //mock data moet van post komen
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.now();
//        String currentDate = dtf.format(localDate);
//        System.out.println(currentDate);
//
//        List<WorklogDTO> worklogs= new ArrayList<>();
//        String basicAuthUserName = "Nielsb01";
//        String basicAuthPass = "OOSEGENUA";
//
//        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 3960, "KNBPU-1"));
//        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 1800, "KNBPU-1"));
//        worklogs.add(new WorklogDTO("JIRAUSER10000", currentDate, 1800, "KNBPU-1"));
//        worklogs.add(new WorklogDTO("JIRAUSER10100", currentDate, 3600, "KNBPU-1"));
//
////        String worker = "JIRAUSER10100"; // niels a
////        String worker = "JIRAUSER10000"; // niels borkes
//        //end of mock
//
//        addWorklog(worklogs, basicAuthUserName, basicAuthPass);
//    }
//
//    /**
//     * Method creates worklog for a user by sending a post request to the Tempo API,
//     * the location of where the worklog should be created is specified by the originTaskId in the {@link WorklogDTO}.
//     * the standard comment of the {@link WorklogDTO} will be "Logging from JavaSyncApp"
//     *
//     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from Jira-server 1 .
//     * @param username for basicAuth.
//     * @param password for basicAuth.
//     */
//    public void addWorklog(List<WorklogDTO> worklogs, String username, String password) {
//
//        String url = "http://127.0.0.1/rest/tempo-timesheets/4/worklogs";
//
//        for(WorklogDTO worklog : worklogs){
//            HttpResponse<JsonNode> response = Unirest.post(url)
//                    .basicAuth(username, password)
//                    .header("Accept", "application/json")
//                    .header("Content-Type", "application/json")
//                    .body(worklog)
//                    .asJson();
//
//            System.out.println(response.getBody());
//        }
//    }
//
//}