package nl.avisi.controller;

import nl.avisi.dto.WorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.WorklogSynchronisation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class SyncControllerTest {

    public static final int HTTP_STATUS_OK = 200;
    private SyncController sut;
    private WorklogSynchronisation mockedWorklogSynchronisation;

    @BeforeEach
    void setUp() {
        sut = new SyncController();
        mockedWorklogSynchronisation = mock(WorklogSynchronisation.class);
        sut.setWorklogSynchronisation(mockedWorklogSynchronisation);
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCheckResponseEntity() {
        // Setup
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        assertEquals("Synchronisatie succesvol", actualValue.getEntity());
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCallsRetrieveWorklogs() {
        // Setup
        List<WorklogDTO> worklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();
        when(mockedWorklogSynchronisation.retrieveWorklogsFromClientServer(worklogRequestDTO)).thenReturn(worklogs);
        when(mockedWorklogSynchronisation.createWorklogsOnAvisiServer(worklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        verify(mockedWorklogSynchronisation).retrieveWorklogsFromClientServer(worklogRequestDTO);
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCallsCreateWorklogsOnAvisiServer() {
        // Setup
        List<WorklogDTO> worklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();
        when(mockedWorklogSynchronisation.retrieveWorklogsFromClientServer(worklogRequestDTO)).thenReturn(worklogs);
        when(mockedWorklogSynchronisation.createWorklogsOnAvisiServer(worklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        verify(mockedWorklogSynchronisation).createWorklogsOnAvisiServer(worklogs);
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCheckResponseStatus() {
        // Setup
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }



}
