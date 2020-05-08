package nl.avisi.model;

import nl.avisi.dto.WorklogRequestDTO;

public class temp {

    public void synchroniseWorklogsFromClientToAvisi(WorklogRequestDTO worklogRequestDTO) {

        createWorklogsInAvisiServer(retrieveWorklogsFromClientServer(worklogRequestDTO));

    }
}
