package nl.avisi.timer;

import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.inject.Inject;
import java.util.Calendar;

public class SynchronisationTimerStarter {

    private JiraSynchronisationProperties synchronisationProperties;
    private TimerStarter timerStarter;

    @Inject
    public void setSynchronisationProperties(JiraSynchronisationProperties synchronisationProperties) {
        this.synchronisationProperties = synchronisationProperties;
    }

    @Inject
    public void setTimerStarter(TimerStarter timerStarter) {
        this.timerStarter = timerStarter;
    }

    public void startSynchronisationTimer() {

        if (timerStarter == null) {
            System.out.println("timerStarter is null");
        }
        if (synchronisationProperties == null) {
            System.out.println("syncprops is null");
        }

        SynchroniseTask synchroniseTask = new SynchroniseTask();
        Calendar synchronisationMoment = Calendar.getInstance(); //synchronisationProperties.getSynchronisationMoment();
        long interval = 1000 * 10; //60 * 60 * 24 * 7;

        if (timerStarter == null) {
            System.out.println("timerStarter is null");
        }
        if (synchronisationProperties == null) {
            System.out.println("syncprops is null");
        }

        if (synchroniseTask == null || synchronisationMoment == null) {
            System.out.println("poepseks");
        }

//        timerStarter.startTimer(synchroniseTask, synchronisationMoment, interval);
    }
}
