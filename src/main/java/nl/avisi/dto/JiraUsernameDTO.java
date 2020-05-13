package nl.avisi.dto;

/**
 * Holds the usernames for both the Jira server instances.
 * These are used to retrieve the matching Jira user keys.
 * A username can either be their username or email address.
 */

public class JiraUsernameDTO {

    /**
     * Username on the client Jira server
     */
    private String clientUsername;

    /**
     * Username on the Avisi Jira server
     */
    private String avisiUsername;

    public String getOriginUsername() {
        return clientUsername;
    }

    public JiraUsernameDTO setOriginUsername(String clientUsername) {
        this.clientUsername = clientUsername;
        return this;
    }

    public String getDestinationUsername() {
        return avisiUsername;
    }

    public JiraUsernameDTO setDestinationUsername(String avisiUsername) {
        this.avisiUsername = avisiUsername;
        return this;
    }
}
