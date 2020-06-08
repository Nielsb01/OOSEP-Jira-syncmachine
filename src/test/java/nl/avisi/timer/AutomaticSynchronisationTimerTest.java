package nl.avisi.timer;

import nl.avisi.datasource.AutomaticSynchronisationDAO;
import nl.avisi.datasource.contracts.IAutomaticSynchronisationDAO;
import nl.avisi.datasource.exceptions.LastSynchronisationDateNotFoundException;
import nl.avisi.logger.ILogger;
import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ejb.Timer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

public class AutomaticSynchronisationTimerTest {

    private AutomaticSynchronisationTimer sut;

    private JiraWorklog mockedJiraWorklog;
    private IAutomaticSynchronisationDAO mockedAutomaticSynchronisationDAO;
    private Timer mockedTimer;
    private ILogger mockedLogger;

    private static final String MOMENT = "2020-03-06 12:00:00";
    private static final String DATE = "2020-03-06";


    @BeforeEach
    void setUp() {
        sut = new AutomaticSynchronisationTimer();

        mockedJiraWorklog = mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);

        mockedAutomaticSynchronisationDAO = mock(AutomaticSynchronisationDAO.class);
        sut.setAutomaticSynchronisationDAO(mockedAutomaticSynchronisationDAO);

        mockedLogger = mock(ILogger.class);
        sut.setLogger(mockedLogger);

        mockedTimer = mock(Timer.class);
    }

    @Test
    void testAutoSynchroniseCallsJiraWorklogAutoSynchroniseWithRightParameters() {
        // Arrange
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
        Mockito.when(mockedAutomaticSynchronisationDAO.getLastSynchronisationMoment()).thenReturn(MOMENT);

        // Act
        sut.autoSynchronise(mockedTimer);

        // Assert
        Mockito.verify(mockedAutomaticSynchronisationDAO).setLastSynchronisationMoment(anyString());
    }

    @Test
    void testAutoSynchroniseSetsLastSyncDateAsCurrentDateIfLastSynchronisationDateNotFoundExceptionIsThrown() {
        // Arrange
        Mockito.when(mockedAutomaticSynchronisationDAO.getLastSynchronisationMoment()).thenThrow(new LastSynchronisationDateNotFoundException());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentMoment = new Date();
        String expectedCurrentDate = dateFormat.format(currentMoment);

        // Act
        sut.autoSynchronise(mockedTimer);

        // Assert
        Mockito.verify(mockedJiraWorklog).autoSynchronisation(expectedCurrentDate, expectedCurrentDate);

    }
}
