package nl.avisi;

import java.util.List;

public class WorklogRequestDTO {
    private String from;
    private String to;
    private List<String> worker;

    public WorklogRequestDTO() {
    }

/*
    public WorklogRequestBody(String from, String to, List<String> worker) {
        this.from = from;
        this.to = to;
        this.worker = worker;
    }
*/

    public String getFrom() {
        return from;
    }

    public WorklogRequestDTO setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public WorklogRequestDTO setTo(String to) {
        this.to = to;
        return this;
    }

    public List<String> getWorker() {
        return worker;
    }

    public WorklogRequestDTO setWorker(List<String> worker) {
        this.worker = worker;
        return this;
    }
}
