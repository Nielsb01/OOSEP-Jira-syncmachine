package nl.avisi.model.worklog_crud;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.api.TempoInterface;
import nl.avisi.dto.DestinationWorklogDTO;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiraWorklogCreator {

    private TempoInterface tempoInterface;

    @Inject
    public void setTempoInterface(TempoInterface tempoInterface) {
        this.tempoInterface = tempoInterface;
    }

    /**
     * Method creates worklog for a user by sending a post request to the Tempo API,
     * the location of where the worklog should be created is specified by the originTaskId in the {@link DestinationWorklogDTO}.
     * the standard comment of the {@link DestinationWorklogDTO} will be "Logging from JavaSyncApp"
     *
     * @param worklogs ArrayList consisting of WorklogDTO's this list are all the worklogs retrieved from client Jira-server.
     * @return A map of worklogDTO's with their corresponding status codes after being posted.
     */
    public Map<DestinationWorklogDTO, Integer> createWorklogsOnDestinationServer(List<DestinationWorklogDTO> worklogs) {
        Map<DestinationWorklogDTO, Integer> responseCodes = new HashMap<>();

        for (DestinationWorklogDTO worklog : worklogs) {
            HttpResponse<JsonNode> response = tempoInterface.createWorklogOnDestinationServer(worklog);
            responseCodes.put(worklog, response.getStatus());
        }

        return responseCodes;
    }
}
