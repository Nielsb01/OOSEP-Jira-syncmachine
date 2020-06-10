package nl.avisi.model.contracts;

import nl.avisi.dto.ManualSyncDTO;
import nl.avisi.dto.SynchronisedDataDTO;

public interface IJiraWorklog {
    SynchronisedDataDTO manualSynchronisation(ManualSyncDTO manualSyncDTO, int userId);

    void autoSynchronisation(String fromDate, String toDate);

    void synchroniseFailedWorklogs();
}
