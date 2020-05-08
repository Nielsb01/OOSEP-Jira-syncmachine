package nl.avisi.model;

import kong.unirest.*;

import kong.unirest.json.JSONException;
import nl.avisi.InvalidEmailException;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyReaders.JiraSynchronisationProperties;

import javax.inject.Inject;

/**
 * Responsible for everything that has to do with the JiraUser
 */
public class JiraUser {

    /**
     * base URL where the Jira server of the client is being hosted
     */
    private String clientUrl;

    /**
     * base URL where the Jira server of Avisi is being hosted
     */
    private String avisiUrl;

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest request;

    /**
     * Contains information for the authentication required to make a HTTP request
     */
    private BasicAuth basicAuth;

    /**
     * is used to read the necessary property information
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
    public void setRequest(IRequest request) {
        this.request = request;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    /**
     * Retrieves and returns the Jira user key that
     * corresponds to the given email address.
     *
     * @param email  the email address supplied by the user that is
     *               linked to a Jira account. This will
     *               be used to retrieve the Jira user key linked to it.
     * @param server The server of which the user wishes to retrieve their
     *               user key. In this instance it's either "Avisi" or "Client".
     * @return the corresponding user key. For example: "JIRAUSER10100"
     */
    public String retrieveJiraUserKeyByEmail(String email, String server) {
        setAvisiUrl(jiraSynchronisationProperties.getDestinationUrl());
        setClientUrl(jiraSynchronisationProperties.getOriginUrl());

        String jiraUserKey = "";

        setBasicAuth(new BasicAuth()
                .setPassword(jiraSynchronisationProperties.getAdminPassword())
                .setUsername(jiraSynchronisationProperties.getAdminUsername()));
        request.setAuthentication(basicAuth);

        String requestUrl = determineUrl(server) + email;

        HttpResponse<JsonNode> JSONJiraUser = request.get(requestUrl);

        jiraUserKey = getJiraUserKeyFromJson(JSONJiraUser);

        if (jiraUserKey.isEmpty()) {
            throw new InvalidEmailException();
        }

        return jiraUserKey;
    }

    /**
     * retrieves the Jira user key from the passed in response object
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
            throw new InvalidEmailException();
        }

        return jiraUserKey;
    }

    /**
     * Determines which url to use for the HTTP request based on the input
     * from the user.
     *
     * @param server The server of which the user wishes to retrieve their
     *               user key. In this instance it's either "Avisi" or "Client".
     * @return The correct url based on which server was passed in by the user.
     */
    private String determineUrl(String server) {
        return server.equalsIgnoreCase("Avisi") ? avisiUrl : clientUrl;
    }
}
