package nl.avisi.dto;

public class JiraUserKeyDTO {
    private String clientUserKey;
    private String AvisiUserKey;

    public String getClientUserKey() {
        return clientUserKey;
    }

    public JiraUserKeyDTO setClientUserKey(String clientUserKey) {
        this.clientUserKey = clientUserKey;
        return this;
    }

    public String getAvisiUserKey() {
        return AvisiUserKey;
    }

    public JiraUserKeyDTO setAvisiUserKey(String avisiUserKey) {
        AvisiUserKey = avisiUserKey;
        return this;
    }
}
