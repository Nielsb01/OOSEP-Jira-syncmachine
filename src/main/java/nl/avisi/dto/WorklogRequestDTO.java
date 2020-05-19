package nl.avisi.dto;

import java.util.List;
/**
 * Class contains the necessary information to
 * request worklogs between two given dates from the origin server
 */
public class WorklogRequestDTO {

    /**
     * is the first date from where in between the worklogs will be requested.
     * if the from date is 2020-04-20 all the worklogs created on or later then this date will be requested
     */
    private String from;

    /**
     * is the second date from where in between the worklogs will be requested.
     * if the from date is 2020-04-25 all the worklogs created between the "from" date and this date will be requested
     */
    private String to;

    /**
     * is a list of workers for whom the worklogs have to be requested "JIRAUSER10000"
     */
    private List<String> worker;

    public WorklogRequestDTO(String from, String to, List<String> worker) {
        this.from = from;
        this.to = to;
        this.worker = worker;
    }

    public WorklogRequestDTO() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getWorker() {
        return worker;
    }

    public void setWorker(List<String> worker) {
        this.worker = worker;
    }
}
