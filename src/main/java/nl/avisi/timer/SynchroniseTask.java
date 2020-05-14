package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class SynchroniseTask extends TimerTask {

    /**
     * Is the owner of the method that is to be performed when the timer finishes
     */
    private JiraWorklog jiraWorklog;

    /**
     * The last date a synchronisation took place
     */
    private Calendar lastSynchronisationDate;

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    /**
     * Calls parent constructor and inits lastSynchronisationDate
     */
    public SynchroniseTask() {
        super();

        setLastSynchronisationDate(Calendar.getInstance());
    }

    /**
     * Is called when the auto sync is scheduled. Runs the jira worklog synchronise method
     */
    @Override
    public void run() {
        if (lastSynchronisationDate == null) {
            setLastSynchronisationDate(Calendar.getInstance());
        }

        Calendar newLastSynchronisationDate = Calendar.getInstance();

        jiraWorklog.synchronise();

        setLastSynchronisationDate(newLastSynchronisationDate);
    }

    private void setLastSynchronisationDate(Calendar newLastSynchronisationDate) {
        this.lastSynchronisationDate = newLastSynchronisationDate;
    }

    public String getLastSynchronisationDate() {
        return castCalendarToString(lastSynchronisationDate);
    }

    private String castCalendarToString(Calendar lastSynchronisationDate) {
        Date lastSynchronisationDateDate = lastSynchronisationDate.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(lastSynchronisationDateDate);
    }
}
