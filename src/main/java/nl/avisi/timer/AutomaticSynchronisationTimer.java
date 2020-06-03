package nl.avisi.timer;

import nl.avisi.datasource.AutomaticSynchronisationDAO;
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

    private AutomaticSynchronisationDAO automaticSynchronisationDAO;

    private JiraWorklog jiraWorklog;

    @Resource
    public void setTimerService(TimerService timerService) {
        this.timerService = timerService;
    }

    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    @Inject
    public void setAutomaticSynchronisationDAO(AutomaticSynchronisationDAO automaticSynchronisationDAO) {
        this.automaticSynchronisationDAO = automaticSynchronisationDAO;
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
        String currentMoment = getCurrentMoment();
        String lastSynchronisationMoment = automaticSynchronisationDAO.getLastSynchronisationMoment();

        jiraWorklog.autoSynchronisation(
                castMomentToDate(lastSynchronisationMoment),
                castMomentToDate(currentMoment)
        );

        updateLastSynchronisationMoment(currentMoment);
    }

    private void updateLastSynchronisationMoment(String newLastSynchronisationMoment) {
        automaticSynchronisationDAO.setLastSynchronisationMoment(newLastSynchronisationMoment);
    }

    private String getCurrentMoment() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date currentMoment = new Date();

        return dateFormat.format(currentMoment);
    }

    private String castMomentToDate(String moment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(moment);
    }
}
