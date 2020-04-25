package nl.avisi;

import kong.unirest.*;

import java.util.ArrayList;
import java.util.List;


public class RetrieveData {

    /**
     *
     * @return Contains all the worklogs that were retrieved within the given date range.
     */
    public List<WorklogDTO> retrieveWorklogs(String from, String to, List<String> workers) {

        WorklogRequestBody worklogRequestBody = new WorklogRequestBody(from, to, workers);
        HttpResponse<JsonNode> JSONWorklogs = requestWorklogs(worklogRequestBody);

        List<WorklogDTO> worklogs = new ArrayList<>();

        for (int i = 0; i < JSONWorklogs.getBody().getArray().length(); i++) {
            String worker = JSONWorklogs.getBody().getArray().getJSONObject(i).getString("worker");
            String started = JSONWorklogs.getBody().getArray().getJSONObject(i).getString("started");
            String originTaskId = JSONWorklogs.getBody().getArray().getJSONObject(i).getJSONObject("issue").getString("accountKey");
            int timeSpentSeconds = JSONWorklogs.getBody().getArray().getJSONObject(i).getInt("timeSpentSeconds");

            worklogs.add(new WorklogDTO(worker, started, timeSpentSeconds, originTaskId));
        }
        return worklogs;
    }


    private HttpResponse<JsonNode> requestWorklogs(WorklogRequestBody requestBody) {
        return Unirest.post("http://127.0.0.1/rest/tempo-timesheets/4/worklogs/search")
                .basicAuth("ruubz2", "xtkWMeAbZcWB6FN")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .asJson();
    }

}
