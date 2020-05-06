package nl.avisi;

import java.util.List;

public class WorklogRequestBody {
    private String from;
    private String to;
    private List<String> worker;

    public WorklogRequestBody() {
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

    public WorklogRequestBody setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public WorklogRequestBody setTo(String to) {
        this.to = to;
        return this;
    }

    public List<String> getWorker() {
        return worker;
    }

    public WorklogRequestBody setWorker(List<String> worker) {
        this.worker = worker;
        return this;
    }
}
