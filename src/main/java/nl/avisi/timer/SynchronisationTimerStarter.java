package nl.avisi.timer;

import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.inject.Inject;
import java.util.Calendar;

public class SynchronisationTimerStarter {

    /**
     * Used to get synchronisation moment
     */
    private JiraSynchronisationProperties synchronisationProperties;

    /**
     * Used to start the synchronisation timer
     */
    private TimerStarter timerStarter;

    /**
     * Contains the task that is to be performed by the timer
     */
    private SynchroniseTask synchroniseTask;

    @Inject
    public void setSynchronisationProperties(JiraSynchronisationProperties synchronisationProperties) {
        this.synchronisationProperties = synchronisationProperties;
    }

    @Inject
    public void setTimerStarter(TimerStarter timerStarter) {
        this.timerStarter = timerStarter;
    }

    @Inject
    public void setSynchroniseTask(SynchroniseTask synchroniseTask) {
        this.synchroniseTask = synchroniseTask;
    }

    /**
     * Starts the synchronisation nl.avisi.timer based on the jira synchronisation config file.
     */
    public void startSynchronisationTimer() {
        Calendar synchronisationMoment = synchronisationProperties.getSynchronisationMoment();
        long interval = 1000 * 60 * 60 * 24 * 7;

        timerStarter.startTimer(synchroniseTask, synchronisationMoment, interval);
    }
}
