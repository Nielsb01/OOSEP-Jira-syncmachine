package nl.avisi.timer;

import nl.avisi.datasource.AutomaticSynchronisationDAO;
import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ejb.Timer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Matchers.anyString;

public class AutomaticSynchronisationTimerTest {

    private AutomaticSynchronisationTimer sut;

    private JiraWorklog mockedJiraWorklog;
    private AutomaticSynchronisationDAO mockedAutomaticSynchronisationDAO;

    private static final String MOMENT = "2020-03-06 12:00:00";
    private static final String DATE = "2020-03-06";


    @BeforeEach
    void setUp() {
        sut = new AutomaticSynchronisationTimer();

        mockedJiraWorklog = Mockito.mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);

        mockedAutomaticSynchronisationDAO = Mockito.mock(AutomaticSynchronisationDAO.class);
        sut.setAutomaticSynchronisationDAO(mockedAutomaticSynchronisationDAO);
    }

    @Test
    void testAutoSynchroniseCallsJiraWorklogAutoSynchroniseWithRightParameters() {
        // Arrange
        Timer mockedTimer = Mockito.mock(Timer.class);

        Mockito.when(mockedAutomaticSynchronisationDAO.getLastSynchronisationMoment()).thenReturn(MOMENT);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentMoment = new Date();
        String expectedCurrentDate = dateFormat.format(currentMoment);

        // Act
        sut.autoSynchronise(mockedTimer);

        // Assert
        Mockito.verify(mockedJiraWorklog).autoSynchronisation(DATE, expectedCurrentDate);
    }

    @Test
    void testAutoSynchroniseCallsAutomaticSynchronisationDAOSetLastSynchronisationMoment() {
        // Arrange
        Timer mockedTimer = Mockito.mock(Timer.class);

        Mockito.when(mockedAutomaticSynchronisationDAO.getLastSynchronisationMoment()).thenReturn(MOMENT);

        // Act
        sut.autoSynchronise(mockedTimer);

        // Assert
        Mockito.verify(mockedAutomaticSynchronisationDAO).setLastSynchronisationMoment(anyString());
    }
}
