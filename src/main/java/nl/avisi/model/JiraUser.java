package nl.avisi.model;

import kong.unirest.*;

import kong.unirest.json.JSONException;
import nl.avisi.datasource.contracts.IUserDAO;
import nl.avisi.exceptions.InvalidUsernameException;
import nl.avisi.dto.JiraUserKeyDTO;
import nl.avisi.dto.JiraUsernameDTO;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.inject.Inject;

/**
 * Responsible for everything that has to do with the JiraUser
 */
public class JiraUser {

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest<BasicAuth> request;

    /**
     * Used for interacting with the database
     */
    private IUserDAO userDAO;

    /**
     * Is used to read the necessary property information
     */
    private JiraSynchronisationProperties jiraSynchronisationProperties;


    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
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

        String originUrl = jiraSynchronisationProperties.getOriginUrl();
        String destinationUrl = jiraSynchronisationProperties.getDestinationUrl();

        createJiraConnection();

        HttpResponse<JsonNode> jsonOriginJiraUser = request.get(originUrl + jiraUsernameDTO.getOriginUsername());
        HttpResponse<JsonNode> jsonDestinationJiraUser = request.get(destinationUrl + jiraUsernameDTO.getDestinationUsername());

        JiraUserKeyDTO jiraUserKeyDTO = createJiraUserKeyDTO(jsonOriginJiraUser, jsonDestinationJiraUser);

        if (jiraUserKeyDTO.getDestinationUserKey().isEmpty() || jiraUserKeyDTO.getOriginUserKey().isEmpty()) {
            throw new InvalidUsernameException();
        }

        return jiraUserKeyDTO;
    }

    private void createJiraConnection() {
        BasicAuth basicAuth = new BasicAuth()
                .setUsername(jiraSynchronisationProperties.getAdminUsername())
                .setPassword(jiraSynchronisationProperties.getAdminPassword());
        request.setAuthentication(basicAuth);
    }

    private JiraUserKeyDTO createJiraUserKeyDTO(HttpResponse<JsonNode> jsonOriginUserKey, HttpResponse<JsonNode> jsonDestinationUserKey) {
        JiraUserKeyDTO jiraUserKeyDTO = new JiraUserKeyDTO();

        String originUserKey = getJiraUserKeyFromJson(jsonOriginUserKey);
        String destinationUserKey = getJiraUserKeyFromJson(jsonDestinationUserKey);

        jiraUserKeyDTO.setOriginUserKey(originUserKey);
        jiraUserKeyDTO.setDestinationUserKey(destinationUserKey);

        return jiraUserKeyDTO;
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

    public void setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO) {
        // TODO aanroep van retrieveJiraUserKeyByUsername en resultaat in de database knallen

        /*
            Deze methode is nog leeg, maar is nodig om de UserController te kunnen testen en te pushen.
            Iemand moet deze in een andere branch verder uitwerken.
         */

    }
}
