package nl.avisi.datasource.contracts;

import nl.avisi.dto.DestinationWorklogDTO;

import java.util.List;

public interface IWorklogDAO {
    List<Integer> getAllWorklogIds();

    void addWorklogId(int worklogId);

    void addFailedworklog(DestinationWorklogDTO worklog, Integer worklogId);
}
