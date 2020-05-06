package nl.avisi;

public class WorklogDTO {
        private String worker;
        private String comment = "Logging from JavaSyncApp";
        private String started;
        private int timeSpentSeconds;
        private String originTaskId;

    public WorklogDTO() {
    }

    /*public WorklogDTO(String worker, String started, int timeSpentSeconds, String originTaskId) {
        this.worker = worker;
        this.comment = "Logging from JavaSyncApp";
        this.started = started;
        this.timeSpentSeconds = timeSpentSeconds;
        this.originTaskId = originTaskId;
    }*/

    public String getWorker() {
        return worker;
    }


    public WorklogDTO setWorker(String worker) {
        this.worker = worker;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public WorklogDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getStarted() {
        return started;
    }

    public WorklogDTO setStarted(String started) {
        this.started = started;
        return this;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public WorklogDTO setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
        return this;
    }

    public String getOriginTaskId() {
        return originTaskId;
    }

    public WorklogDTO setOriginTaskId(String originTaskId) {
        this.originTaskId = originTaskId;
        return this;
    }
}
