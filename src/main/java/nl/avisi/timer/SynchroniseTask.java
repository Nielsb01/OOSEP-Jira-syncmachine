package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;

import javax.inject.Inject;
import java.util.TimerTask;

public class SynchroniseTask extends TimerTask {

    private JiraWorklog jiraWorklog;

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    /**
     * Is called when the auto sync is scheduled. Runs the jira worklog synchronise method
     */
    @Override
    public void run() {
        jiraWorklog.synchronise();
    }
}
