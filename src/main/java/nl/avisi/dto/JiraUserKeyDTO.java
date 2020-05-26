package nl.avisi.dto;

public class JiraUserKeyDTO {
    private String originUserKey;
    private String destinationUserKey;

    public JiraUserKeyDTO() {
        //
    }

    public JiraUserKeyDTO(String originUserKey, String destinationUserKey) {
        this.originUserKey = originUserKey;
        this.destinationUserKey = destinationUserKey;
    }

    public String getOriginUserKey() {
        return originUserKey;
    }

    public JiraUserKeyDTO setOriginUserKey(String originUserKey) {
        this.originUserKey = originUserKey;
        return this;
    }

    public String getDestinationUserKey() {
        return destinationUserKey;
    }

    public JiraUserKeyDTO setDestinationUserKey(String destinationUserKey) {
        this.destinationUserKey = destinationUserKey;
        return this;
    }
}
