package nl.avisi;

import java.util.List;

public class WorklogRequestBody {
    private String from;
    private String to;
    private List<String> worker;

    public WorklogRequestBody() {
    }

    public WorklogRequestBody(String from, String to, List<String> worker) {
        this.from = from;
        this.to = to;
        this.worker = worker;
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
