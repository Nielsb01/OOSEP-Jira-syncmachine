package nl.avisi.timer;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class TimerMain {

    public TimerMain() {
        SynchronisationTimerStarter synchronisationTimerStarter = new SynchronisationTimerStarter();
        synchronisationTimerStarter.startSynchronisationTimer();
    }
}
