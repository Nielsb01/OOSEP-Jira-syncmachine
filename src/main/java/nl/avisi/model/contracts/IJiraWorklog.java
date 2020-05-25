package nl.avisi.model.contracts;

import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.authentication.BasicAuth;

import java.util.List;
import java.util.Map;

public interface IJiraWorklog {
    List<OriginWorklogDTO> retrieveWorklogsFromOriginServer(WorklogRequestDTO worklogRequestDTO);
    Map<DestinationWorklogDTO, Integer> createWorklogsOnDestinationServer(List<DestinationWorklogDTO> worklogs);
    void manualSynchronisation(WorklogRequestDTO worklogRequestDTO, int userId);
    void synchronise();
}
