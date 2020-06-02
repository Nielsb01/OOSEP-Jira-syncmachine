package nl.avisi.model.contracts;

import nl.avisi.dto.ManualSyncDTO;

public interface IJiraWorklog {
    void manualSynchronisation(ManualSyncDTO manualSyncDTO, int userId);

    void autoSynchronisation(String fromDate, String toDate);
}
