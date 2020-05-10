package nl.avisi.dto;

public class JiraUsernameDTO {
    private String clientUsername;
    private String AvisiUsername;

    public String getClientUsername() {
        return clientUsername;
    }

    public JiraUsernameDTO setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
        return this;
    }

    public String getAvisiUsername() {
        return AvisiUsername;
    }

    public JiraUsernameDTO setAvisiUsername(String avisiUsername) {
        AvisiUsername = avisiUsername;
        return this;
    }
}
