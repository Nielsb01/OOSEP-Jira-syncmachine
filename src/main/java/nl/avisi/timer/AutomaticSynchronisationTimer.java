package nl.avisi.timer;

import nl.avisi.datasource.contracts.IAutomaticSynchronisationDAO;
import nl.avisi.datasource.exceptions.LastSynchronisationDateNotFoundException;
import nl.avisi.logger.ILogger;
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

    private IAutomaticSynchronisationDAO automaticSynchronisationDAO;

    private JiraWorklog jiraWorklog;

    /**
     * responsible for logging errors
     */
    private ILogger logger;

    @Resource
    public void setTimerService(TimerService timerService) {
        this.timerService = timerService;
    }

    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    @Inject
    public void setAutomaticSynchronisationDAO(IAutomaticSynchronisationDAO automaticSynchronisationDAO) {
        this.automaticSynchronisationDAO = automaticSynchronisationDAO;
    }

    @Inject
    public void setJiraWorklog(JiraWorklog jiraWorklog) {
        this.jiraWorklog = jiraWorklog;
    }

    @Inject
    public void setLogger(ILogger logger) {
        this.logger = logger;
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

    /**
     * Bootstraps the automatic synchronisation process
     * @param timer The JEE timer that starts this function
     */
    @Timeout
    public void autoSynchronise(Timer timer) {
        String currentMoment = getCurrentMoment();
        String lastSynchronisationMoment = getLastSynchronisationMoment();

        jiraWorklog.autoSynchronisation(
                castMomentToDate(lastSynchronisationMoment),
                castMomentToDate(currentMoment)
        );

        jiraWorklog.synchroniseFailedWorklogs();

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

    private String getLastSynchronisationMoment() {
        String lastSynchronisationMoment;

        try {
            lastSynchronisationMoment = automaticSynchronisationDAO.getLastSynchronisationMoment();
        } catch (LastSynchronisationDateNotFoundException e) {
            logger.logToDatabase(getClass().getName(), "getLastSynchronisationMoment", e);
            lastSynchronisationMoment = getCurrentMoment();
        }

        return lastSynchronisationMoment;
    }

    private String castMomentToDate(String moment) {
        return moment.substring(0, 10);
    }
}
