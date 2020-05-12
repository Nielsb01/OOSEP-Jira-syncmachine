package nl.avisi.timer;

import java.util.Calendar;
import java.util.TimerTask;

public class TimerStarter {

    public void startTimer(TimerTask task, Calendar moment, long delay) {
        EventTimer synchronisationTimer = new EventTimer(
                task,
                moment,
                delay);
    }
}
