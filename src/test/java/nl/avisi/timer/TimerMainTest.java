package nl.avisi.timer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TimerMainTest {

    TimerMain sut;
    SynchronisationTimerStarter mockedSynchronisationTimerStarter;

    @BeforeEach
    void setUp() {
        sut = new TimerMain();

        mockedSynchronisationTimerStarter = Mockito.mock(SynchronisationTimerStarter.class);
        sut.setSynchronisationTimerStarter(mockedSynchronisationTimerStarter);
    }

    @Test
    void testStartSynchronisationTimerCallsSynchronisationTimerStarterStartSynchronisationTimer() {
        // Arrange

        // Act
        sut.startSynchronisationTimer();

        // Assert
        Mockito.verify(mockedSynchronisationTimerStarter).startSynchronisationTimer();
    }
}
