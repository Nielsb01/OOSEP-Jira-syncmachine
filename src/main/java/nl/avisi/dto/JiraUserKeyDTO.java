package nl.avisi.dto;

public class JiraUserKeyDTO {
    private String clientUserKey;
    private String avisiUserKey;

    public String getClientUserKey() {
        return clientUserKey;
    }

    public JiraUserKeyDTO setClientUserKey(String clientUserKey) {
        this.clientUserKey = clientUserKey;
        return this;
    }

    public String getAvisiUserKey() {
        return avisiUserKey;
    }

    public JiraUserKeyDTO setAvisiUserKey(String avisiUserKey) {
        this.avisiUserKey = avisiUserKey;
        return this;
    }
}
