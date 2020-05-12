package nl.avisi.timer;

import nl.avisi.propertyreaders.JiraSynchronisationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class SynchronisationTimerStarterTest {

    SynchronisationTimerStarter sut;
    JiraSynchronisationProperties mockedJiraSynchronisationProperties;
    TimerStarter mockedTimerStarter;

    @BeforeEach
    void setUp() {
        sut = new SynchronisationTimerStarter();

        mockedJiraSynchronisationProperties = Mockito.mock(JiraSynchronisationProperties.class);
        sut.setSynchronisationProperties(mockedJiraSynchronisationProperties);

        mockedTimerStarter = Mockito.mock(TimerStarter.class);
        sut.setTimerStarter(mockedTimerStarter);
    }

    @Test
    void testStartSynchronisationCallsTimerStarterStartTimer() {
        // Arrange
        Calendar expectedCalendar = Calendar.getInstance();
        long expectedInterval = 1000 * 60 * 60 * 24 * 7;

        Mockito.when(mockedJiraSynchronisationProperties.getSynchronisationMoment()).thenReturn(expectedCalendar);

        // Act
        sut.startSynchronisationTimer();

        // Assert
        Mockito.verify(mockedTimerStarter).startTimer(any(SynchroniseTask.class), eq(expectedCalendar), eq(expectedInterval));
    }
}
