package nl.avisi;

import nl.avisi.network.IRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


class TimeWriteResourceTest {

    private TimeWriteResource sut;
    private IRequest mockedRequest;
    private HttpResponse response;

    @BeforeEach
    public void setup(){
         sut = new TimeWriteResource();
         mockedRequest = mock(IRequest.class);
         response = mock(HttpResponse.class);


         sut.setRequest(mockedRequest);
    }

    @Test
    public void whileAddingWorklogsCheckMapIsSameLengthAsWorklogs() {
        // Arrange
        List<WorklogDTO> mockWorklogs= new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-2"));

        when(mockedRequest.post(any(),any())).thenReturn(response);
        when(response.getStatus()).thenReturn(200);

        // Act
        Map actualvalue = sut.addWorklog(mockWorklogs,adminAuthUserName,adminAuthPass,"http://127.0.0.1/rest/tempo-timesheets/4/worklogs");

        //Assert
        assertEquals(2,actualvalue.size());
    }

    @Test
    public void whileAddingWorklogsCheckMapNotAllStatuscodes200() {
        // Arrange
        List<WorklogDTO> mockWorklogs= new ArrayList<>();
        String adminAuthUserName = "Nielsb01";
        String adminAuthPass = "OOSEGENUA";

        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10000").setStarted("2020-05-07").setTimeSpentSeconds(660).setOriginTaskId("KNBPU-2"));
        mockWorklogs.add(new WorklogDTO().setWorker("JIRAUSER10100").setStarted("2020-05-07").setTimeSpentSeconds(840).setOriginTaskId("KNBPU-4"));

        when(mockedRequest.post(any(),any())).thenReturn(response);
        when(response.getStatus()).thenReturn(200,400);

        // Act
        Map actualvalue = sut.addWorklog(mockWorklogs,adminAuthUserName,adminAuthPass,"http://127.0.0.1/rest/tempo-timesheets/4/worklogs");

        //Assert
        assertTrue(actualvalue.containsValue(400));
        assertTrue(actualvalue.containsValue(200));
    }
}