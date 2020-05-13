package nl.avisi.controller;

import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.OriginWorklogDTO;
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
        List<OriginWorklogDTO> originWorklogs = new ArrayList<>();
        List<DestinationWorklogDTO> destinationWorklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        when(mockedJiraWorklog.retrieveWorklogsFromClientServer(worklogRequestDTO)).thenReturn(originWorklogs);
        when(mockedJiraWorklog.transformFromOriginToDestination(originWorklogs)).thenReturn(destinationWorklogs);
        when(mockedJiraWorklog.createWorklogsOnAvisiServer(destinationWorklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        verify(mockedJiraWorklog).retrieveWorklogsFromClientServer(worklogRequestDTO);
    }

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCallsCreateWorklogsOnAvisiServer() {
        // Setup
        List<OriginWorklogDTO> originWorklogs = new ArrayList<>();
        List<DestinationWorklogDTO> destinationWorklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        when(mockedJiraWorklog.retrieveWorklogsFromClientServer(worklogRequestDTO)).thenReturn(originWorklogs);
        when(mockedJiraWorklog.transformFromOriginToDestination(originWorklogs)).thenReturn(destinationWorklogs);
        when(mockedJiraWorklog.createWorklogsOnAvisiServer(destinationWorklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        verify(mockedJiraWorklog).createWorklogsOnAvisiServer(destinationWorklogs);
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

    @Test
    void testsynchroniseWorklogsFromClientToAvisiCallsTransformFromOriginToDestination() {
        // Setup
        List<OriginWorklogDTO> originWorklogs = new ArrayList<>();
        List<DestinationWorklogDTO> destinationWorklogs = new ArrayList<>();
        WorklogRequestDTO worklogRequestDTO = new WorklogRequestDTO();

        when(mockedJiraWorklog.retrieveWorklogsFromClientServer(worklogRequestDTO)).thenReturn(originWorklogs);
        when(mockedJiraWorklog.transformFromOriginToDestination(originWorklogs)).thenReturn(destinationWorklogs);
        when(mockedJiraWorklog.createWorklogsOnAvisiServer(destinationWorklogs)).thenReturn(new HashMap());

        // Run the test
        Response actualValue = sut.synchroniseWorklogsFromClientToAvisi(worklogRequestDTO);

        // Verify the results
        verify(mockedJiraWorklog).transformFromOriginToDestination(originWorklogs);
    }

}
