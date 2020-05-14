package nl.avisi.controller;

import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class SyncControllerTest {

    public static final int HTTP_STATUS_OK = 200;
    private SyncController sut;
    private JiraWorklog mockedJiraWorklog;

    @BeforeEach
    void setUp() {
        sut = new SyncController();
        mockedJiraWorklog = mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);
    }

    @Test
    void testsynchroniseWorklogsFromOriginToDestinationCheckResponseEntity() {
        // Setup
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromOriginToDestination(worklogRequestDTO);

        // Verify the results
        assertEquals("Synchronisatie succesvol", actualValue.getEntity());
    }

    @Test
    void testsynchroniseWorklogsFromOriginToDestinationCallsRetrieveWorklogs() {
        // Setup
        List<DestinationWorklogDTO> worklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();
        when(mockedJiraWorklog.retrieveWorklogsFromOriginServer(worklogRequestDTO)).thenReturn(worklogs);
        when(mockedJiraWorklog.createWorklogsOnDestinationServer(worklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromOriginToDestination(worklogRequestDTO);

        // Verify the results
        verify(mockedJiraWorklog).retrieveWorklogsFromOriginServer(worklogRequestDTO);
    }

    @Test
    void testSynchroniseWorklogsFromOriginToDestinationCallsCreateWorklogsOnDestinationServer() {
        // Setup
        List<DestinationWorklogDTO> worklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();
        when(mockedJiraWorklog.retrieveWorklogsFromOriginServer(worklogRequestDTO)).thenReturn(worklogs);
        when(mockedJiraWorklog.createWorklogsOnDestinationServer(worklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromOriginToDestination(worklogRequestDTO);

        // Verify the results
        verify(mockedJiraWorklog).createWorklogsOnDestinationServer(worklogs);
    }

    @Test
    void testsynchroniseWorklogsFromOriginToDestinationCheckResponseStatus() {
        // Setup
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromOriginToDestination(worklogRequestDTO);

        // Verify the results
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }
}
