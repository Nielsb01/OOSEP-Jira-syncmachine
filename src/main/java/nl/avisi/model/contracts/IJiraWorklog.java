package nl.avisi.model.contracts;

import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.ManualSyncDTO;
import nl.avisi.dto.OriginWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.authentication.BasicAuth;

import java.util.List;
import java.util.Map;

public interface IJiraWorklog {
    void manualSynchronisation(ManualSyncDTO manualSyncDTO, int userId);

    void autoSynchronisation(String fromDate, String toDate);
}
