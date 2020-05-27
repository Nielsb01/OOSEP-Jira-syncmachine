package nl.avisi.model;

import kong.unirest.*;

import kong.unirest.json.JSONException;
import nl.avisi.api.JiraInterface;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.dto.UserPreferenceDTO;
import nl.avisi.model.exceptions.InvalidUsernameException;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.model.contracts.IJiraUser;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

/**
 * Responsible for everything that has to do with the JiraUser
 */
public class JiraUser implements IJiraUser {

    /**
     * Used for interacting with the database
     */
    private IUserDAO userDAO;

    private JiraInterface jiraInterface;

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setJiraInterface(JiraInterface jiraInterface) {
        this.jiraInterface = jiraInterface;
    }

    /**
     * Retrieves the Jira user keys corresponding to
     * the given usernames, by doing a request
     * to the Jira API.
     *
     * @param jiraUsernameDTO Contains usernames for both jira servers.
     *                        Both the username as well as the email address can
     *                        be used.
     * @return JiraUserKeyDTO Contains the matching user keys to the given usernames.
     */
    public JiraUserKeyDTO retrieveJiraUserKeyByUsername(JiraUsernameDTO jiraUsernameDTO) {

        HttpResponse<JsonNode> jsonOriginJiraUser = jiraInterface.getOriginUserKey(jiraUsernameDTO.getOriginUsername());
        HttpResponse<JsonNode> jsonDestinationJiraUser = jiraInterface.getDestinationUserKey(jiraUsernameDTO.getDestinationUsername());

        if (jsonOriginJiraUser.getStatus() != 200 || jsonDestinationJiraUser.getStatus() != 200) {
            throw new InternalServerErrorException();
        }

        JiraUserKeyDTO jiraUserKeyDTO = createJiraUserKeyDTO(jsonOriginJiraUser, jsonDestinationJiraUser);

        if (jiraUserKeyDTO.getDestinationUserKey().isEmpty() || jiraUserKeyDTO.getOriginUserKey().isEmpty()) {
            throw new InvalidUsernameException();
        }

        return jiraUserKeyDTO;
    }

    /**
     * Get all preferences from a user
     *
     * @param userId the user for which to get the preferences
     * @return the preferences for the user
     */
    public UserPreferenceDTO getAutoSyncPreference(int userId) {
        return userDAO.getUserAutoSyncPreference(userId);
    }

    private JiraUserKeyDTO createJiraUserKeyDTO(HttpResponse<JsonNode> jsonOriginUserKey, HttpResponse<JsonNode> jsonDestinationUserKey) {
        String originUserKey = getJiraUserKeyFromJson(jsonOriginUserKey);
        String destinationUserKey = getJiraUserKeyFromJson(jsonDestinationUserKey);

        return new JiraUserKeyDTO(originUserKey, destinationUserKey);
    }

    /**
     * Retrieves the Jira user key from the passed in response object
     *
     * @param jsonJiraUser All data that was retrieved from the HTTP request relating to the
     *                     specified email address
     * @return A string containing the Jira user key
     */
    private String getJiraUserKeyFromJson(HttpResponse<JsonNode> jsonJiraUser) {
        String jiraUserKey;

        try {
            jiraUserKey = jsonJiraUser.getBody().getArray().getJSONObject(0).getString("key");
        } catch (JSONException e) {
            throw new InvalidUsernameException();
        }

        return jiraUserKey;
    }

    /**
     * Responsible for making the appropriate calls
     * to update the users auto synchronisation preference
     *
     * @param userId     Id of the user that made the request
     * @param autoSyncOn Boolean value indicating whether or not
     *                   the user wants their auto synchronisation
     *                   to be on or not
     */
    public void setAutoSyncPreference(int userId, boolean autoSyncOn) {
        userDAO.setAutoSyncPreference(userId, autoSyncOn);
    }

    /**
     * Handles retrieving the correct user keys with
     * the given usernames and passes them on to
     * be persisted in the database
     *
     * @param jiraUsernameDTO Contain the usernames used to retrieve the
     *                        correct user keys
     * @param userId Id of the user that made the request
     */
    public void setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO, int userId) {
        userDAO.updateJiraUserKeys(retrieveJiraUserKeyByUsername(jiraUsernameDTO), userId);
    }
}
