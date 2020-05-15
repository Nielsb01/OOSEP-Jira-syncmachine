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

    public int getWorklogId() {
        return worklogId;
    }

    public OriginWorklogDTO setWorklogId(int worklogId) {
        this.worklogId = worklogId;
        return this;
    }
}
