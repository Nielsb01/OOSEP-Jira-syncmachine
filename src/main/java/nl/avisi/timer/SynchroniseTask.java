package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;

import javax.inject.Inject;
import java.util.TimerTask;

public class SynchroniseTask extends TimerTask {

    JiraWorklog jiraWorklog;

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    @Override
    public void run() {
        jiraWorklog.synchronise();
    }
}
