package nl.avisi.dto;

/**
 * Holds the usernames for both the Jira server instances.
 * These are used to retrieve the matching Jira user keys.
 * A username can either be their username or email address.
 */

public class JiraUsernameDTO {

    /**
     * Username on the origin Jira server
     */
    private String originUsername;

    /**
     * Username on the destination Jira server
     */
    private String destinationUsername;

    public String getOriginUsername() {
        return originUsername;
    }

    public JiraUsernameDTO setOriginUsername(String originUsername) {
        this.originUsername = originUsername;
        return this;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public JiraUsernameDTO setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
        return this;
    }
}
