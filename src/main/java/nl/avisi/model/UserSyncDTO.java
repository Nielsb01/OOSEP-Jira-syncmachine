package nl.avisi.model;

public class UserSyncDTO {
    private String fromWorker;
    private String toWorker;

    public String getFromWorker() {
        return fromWorker;
    }

    public UserSyncDTO setFromWorker(String fromWorker) {
        this.fromWorker = fromWorker;
        return this;
    }

    public String getToWorker() {
        return toWorker;
    }

    public UserSyncDTO setToWorker(String toWorker) {
        this.toWorker = toWorker;
        return this;
    }
}
