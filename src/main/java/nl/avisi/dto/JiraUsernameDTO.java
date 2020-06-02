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

    public JiraUsernameDTO() {
    }

    public JiraUsernameDTO(String originUsername, String destinationUsername) {
        this.originUsername = originUsername;
        this.destinationUsername = destinationUsername;
    }

    public String getOriginUsername() {
        return originUsername;
    }

    public void setOriginUsername(String originUsername) {
        this.originUsername = originUsername;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public void setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
    }
}
