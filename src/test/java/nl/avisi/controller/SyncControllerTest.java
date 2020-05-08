package nl.avisi.controller;

import nl.avisi.model.RetrieveData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class SyncControllerTest {

    private SyncController sut;

    @BeforeEach
    void setUp() {
        sut = new SyncController();
        mocked mock(RetrieveData.class)

    }

    @Test
    void testSyncWorklogs() {
        // Setup

        // Run the test
        sut.syncWorklogs();

        // Verify the results
    }
}
