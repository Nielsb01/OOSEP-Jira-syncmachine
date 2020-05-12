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
     * Base URL where the Jira server of the client is being hosted
     */
    private String clientUrl;

    /**
     * Base URL where the Jira server of Avisi is being hosted
     */
    private String avisiUrl;

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest<BasicAuth> request;

    /**
     * Used for interacting with the database
     */
    private IUserDAO userDAO;

    /**
     * Contains information for the authentication required to make a HTTP request
     */
    private BasicAuth basicAuth;

    /**
     * Is used to read the necessary property information
     */
    private JiraSynchronisationProperties jiraSynchronisationProperties;


    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = String.format("%s/rest/api/2/user/search?username=", clientUrl);
    }

    public void setAvisiUrl(String avisiUrl) {
        this.avisiUrl = String.format("%s/rest/api/2/user/search?username=", avisiUrl);
    }

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
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
        setAvisiUrl(jiraSynchronisationProperties.getDestinationUrl());
        setClientUrl(jiraSynchronisationProperties.getOriginUrl());

        JiraUserKeyDTO jiraUserKeyDTO = new JiraUserKeyDTO();

        setBasicAuth(new BasicAuth()
                .setPassword(jiraSynchronisationProperties.getAdminPassword())
                .setUsername(jiraSynchronisationProperties.getAdminUsername()));
        request.setAuthentication(basicAuth);

        HttpResponse<JsonNode> JSONClientJiraUser = request.get(clientUrl + jiraUsernameDTO.getClientUsername());
        HttpResponse<JsonNode> JSONAvisiJiraUser = request.get(avisiUrl + jiraUsernameDTO.getAvisiUsername());

        jiraUserKeyDTO.setAvisiUserKey(getJiraUserKeyFromJson(JSONAvisiJiraUser));
        jiraUserKeyDTO.setClientUserKey(getJiraUserKeyFromJson(JSONClientJiraUser));

        if (jiraUserKeyDTO.getAvisiUserKey().isEmpty() || jiraUserKeyDTO.getClientUserKey().isEmpty()) {
            throw new InvalidUsernameException();
        }

        return jiraUserKeyDTO;
    }

    /**
     * Retrieves the Jira user key from the passed in response object
     *
     * @param JSONJiraUser All data that was retrieved from the HTTP request relating to the
     *                     specified email address
     * @return A string containing the Jira user key
     */
    private String getJiraUserKeyFromJson(HttpResponse<JsonNode> JSONJiraUser) {
        String jiraUserKey;

        try {
            jiraUserKey = JSONJiraUser.getBody().getArray().getJSONObject(0).getString("key");
        } catch (JSONException e) {
            throw new InvalidUsernameException();
        }

        return jiraUserKey;
    }

    public void setJiraUserKeys(JiraUsernameDTO jiraUsernameDTO) {
        //TODO aanroep van retrieveJiraUserKeyByUsername en resultaat in de database knallen

        /*
            Deze methode is nog leeg, maar is nodig om de UserController te kunnen testen en te pushen.
            Iemand moet deze in een andere branch verder uitwerken.
         */

    }
}
