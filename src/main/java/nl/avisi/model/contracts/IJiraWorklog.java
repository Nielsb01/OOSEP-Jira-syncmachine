package nl.avisi.model.contracts;

import nl.avisi.dto.WorklogRequestDTO;

public interface IJiraWorklog {
    void manualSynchronisation(WorklogRequestDTO worklogRequestDTO, int userId);
    void synchronise();
}
