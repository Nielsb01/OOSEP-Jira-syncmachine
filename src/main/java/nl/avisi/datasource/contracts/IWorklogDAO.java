package nl.avisi.datasource.contracts;

import nl.avisi.dto.DestinationWorklogDTO;

import java.util.List;
import java.util.Map;

public interface IWorklogDAO {
    List<Integer> getAllWorklogIds();

    void addWorklogId(int worklogId);

    void addFailedworklog(Integer worklogId, DestinationWorklogDTO worklog);

    Map<Integer, DestinationWorklogDTO> getAllFailedWorklogs();

    void deleteFailedWorklog(Integer worklogId);
}
