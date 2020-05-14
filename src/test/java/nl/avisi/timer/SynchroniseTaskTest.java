package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;

public class SynchroniseTaskTest {

    SynchroniseTask sut;

    JiraWorklog mockedJiraWorklog;

    @BeforeEach
    void setUp() {
        sut = new SynchroniseTask();

        mockedJiraWorklog = Mockito.mock(JiraWorklog.class);
        sut.setJiraWorklog(mockedJiraWorklog);
    }

    @Test
    void testRunCallsSynchronise() {
        // Arrange

        // Act
        sut.run();

        // Assert
        Mockito.verify(mockedJiraWorklog).synchronise();
    }
}
