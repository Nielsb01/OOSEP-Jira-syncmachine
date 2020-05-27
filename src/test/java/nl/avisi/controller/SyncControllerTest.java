package nl.avisi.controller;


import nl.avisi.dto.ManualSyncDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class SyncControllerTest {

    public static final int HTTP_STATUS_OK = 200;
    private SyncController sut;
    private JiraWorklog mockedJiraWorklog;
    private final static int USER_ID = 1;

    @BeforeEach
    void setUp() {
        sut = new SyncController();
        mockedJiraWorklog = mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);
    }

    @Test
    void testManualSynchronisationChecksResponseStatus() {
        //Arrange
        ManualSyncDTO manualSyncDTO = new ManualSyncDTO();

        //Act
        Response actualValue = sut.manualSynchronisation(manualSyncDTO, USER_ID);

        //Assert
        assertEquals(HTTP_STATUS_OK, actualValue.getStatus());
    }

    @Test
    void testManualSynchronisationCallsManualSynchronisation() {
        //Arrange
        ManualSyncDTO manualSyncDTO = new ManualSyncDTO();
        //Act
        Response actualValue = sut.manualSynchronisation(manualSyncDTO, USER_ID);

        //Assert
        verify(mockedJiraWorklog).manualSynchronisation(manualSyncDTO, USER_ID);
    }
}
