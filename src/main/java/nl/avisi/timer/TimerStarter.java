package nl.avisi.timer;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimerStarter {

    /**
     * Creates and starts a java nl.avisi.timer
     * @param task The task object that should be performed once the nl.avisi.timer reaches its scheduled moment
     * @param moment The first moment when the task should be performed as a Calendar
     * @param interval The time in milliseconds between the first moment the task should be performed and subsequent moments
     */
    public void startTimer(TimerTask task, Calendar moment, long interval) {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(
                task,
                moment.getTime(),
                interval
        );
    }
}
