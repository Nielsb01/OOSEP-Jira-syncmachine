package nl.avisi.timer;

import nl.avisi.model.JiraWorklog;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
@Startup
public class AutomaticSynchronisationTimer {

    private TimerService timerService;

    private JiraSynchronisationProperties jiraSynchronisationProperties;

    private JiraWorklog jiraWorklog;

    private String lastSynchronisationDate;

    @Resource
    public void setTimerService(TimerService timerService) {
        this.timerService = timerService;
    }

    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    @PostConstruct
    private void postConstruct() {
        timerService.createCalendarTimer(createSchedule());
    }

    private ScheduleExpression createSchedule(){
        ScheduleExpression scheduleExpression = new ScheduleExpression();

        scheduleExpression.dayOfWeek(jiraSynchronisationProperties.getSynchronisationDayOfWeek());
        scheduleExpression.hour(jiraSynchronisationProperties.getSynchronisationHour());
        scheduleExpression.minute(jiraSynchronisationProperties.getSynchronisationMinute());
        scheduleExpression.second("0");

        return scheduleExpression;
    }

    @Timeout
    public void autoSynchronise(Timer timer) {
        String currentDate = getCurrentDate();

        jiraWorklog.autoSynchronisation(lastSynchronisationDate, currentDate);
        lastSynchronisationDate = currentDate;

    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();

        return dateFormat.format(currentDate);
    }
}
