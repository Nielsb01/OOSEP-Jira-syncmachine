package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

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

    @Test
    void testConstructorSetsLastSynchronisationDate() {
        // Arrange
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expected = dateFormat.format(date);

        // Act
        String actual = sut.getLastSynchronisationDate();

        // Assert
        assertEquals(expected, actual);
    }
}
