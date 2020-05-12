package nl.avisi.timer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class TimerMain {

    private SynchronisationTimerStarter synchronisationTimerStarter;

    @Inject
    public void setSynchronisationTimerStarter(SynchronisationTimerStarter synchronisationTimerStarter) {
        this.synchronisationTimerStarter = synchronisationTimerStarter;
    }

    /**
     * Bootstraps the jira automatic synchronisation nl.avisi.timer
     */
    @PostConstruct
    public void startSynchronisationTimer() {
        synchronisationTimerStarter.startSynchronisationTimer();
    }
}
