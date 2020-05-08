package nl.avisi.controller;

import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.WorklogSynchronisation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

class SyncControllerTest {

    private SyncController sut;
    private WorklogSynchronisation mockedWorklogSynchronisation;

    @BeforeEach
    void setUp() {
        sut = new SyncController();
        mockedWorklogSynchronisation = mock(WorklogSynchronisation.class);
        sut.setWorklogSynchronisation(mockedWorklogSynchronisation);
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCallsRetrieveWorklogs() {
        // Setup
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        assertEquals("Synchronisatie succesvol", actualValue.getEntity());
    }
}
