package nl.avisi.dto;

import java.util.List;

public class WorklogRequestDTO {
    private String origin;
    private String destination;
    private List<String> worker;
    
    public String getOrigin() {
        return origin;
    }

    public WorklogRequestDTO setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public WorklogRequestDTO setDestination(String destination) {
        this.destination = destination;
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
