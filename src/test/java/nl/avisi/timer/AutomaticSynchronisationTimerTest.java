package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ejb.Timer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


public class AutomaticSynchronisationTimerTest {

    private AutomaticSynchronisationTimer sut;

    private JiraWorklog mockedJiraWorklog;

    @BeforeEach
    void setUp() {
        sut = new AutomaticSynchronisationTimer();

        mockedJiraWorklog = Mockito.mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);
    }

    @Test
    void testAutoSynchroniseCallsJiraWorklogSynchronise() {
        // Arrange
        Timer mockedTimer = Mockito.mock(Timer.class);

        // Act
        sut.autoSynchronise(mockedTimer);

        // Assert
        verify(mockedJiraWorklog).autoSynchronisation(any(), any());
    }
}
