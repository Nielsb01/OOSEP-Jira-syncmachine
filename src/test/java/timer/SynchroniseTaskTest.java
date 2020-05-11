package timer;

import nl.avisi.timer.SynchroniseTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SynchroniseTaskTest {

    SynchroniseTask sut;

    @BeforeEach
    void setUp() {
        sut = new SynchroniseTask();
    }

    @Test
    void testRunCallsSynchronise() {
        // Arrange

        // Act
        sut.run();

        // Assert
    }
}
