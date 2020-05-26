package nl.avisi.dto;

/**
 * Class inherits from InsertWorklogDTO and
 * adds a worklogId. This class will be used to
 * persist the worklogId in the database and compare
 * with other worklogs.
 */
public class OriginWorklogDTO extends DestinationWorklogDTO {

    /**
     * Id of the worklog that was retrieved from the origin server
     */
    private int worklogId;

    public OriginWorklogDTO() {
    }

    public OriginWorklogDTO(String worker, String started, int timeSpentSeconds, String originTaskId, int worklogId) {
        super(worker, started, timeSpentSeconds, originTaskId);
        this.worklogId = worklogId;
    }

    public int getWorklogId() {
        return worklogId;
    }

    public void setWorklogId(int worklogId) {
        this.worklogId = worklogId;
    }
}
