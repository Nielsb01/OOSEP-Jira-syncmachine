package nl.avisi.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class JiraInterface {

    /**
     * Method by which HTTP requests are sent
     */
    private IRequest<BasicAuth> request;

    private JiraSynchronisationProperties jiraSynchronisationProperties;

    @Inject
    public void setRequest(IRequest<BasicAuth> request) {
        this.request = request;
    }

    @Inject
    public void setJiraSynchronisationProperties(JiraSynchronisationProperties jiraSynchronisationProperties) {
        this.jiraSynchronisationProperties = jiraSynchronisationProperties;
    }

    /**
     * Fetches the corresponding jira user key from the origin server based on the given username
     * @param jiraUsername Username which's userkey should be returned
     * @return The jira user key that corresponds to the given jiraUsername in json format
     */
    public HttpResponse<JsonNode> getOriginUserKey(String jiraUsername) {
        return getUserKey(jiraUsername, jiraSynchronisationProperties.getOriginUrl());
    }

    /**
     * Fetches the corresponding jira user key from the destination server based on the given username
     * @param jiraUsername Username which's user key should be returned
     * @return The jira user key that corresponds to the given jiraUsername in json format
     */
    public HttpResponse<JsonNode> getDestinationUserKey(String jiraUsername) {
        return getUserKey(jiraUsername, jiraSynchronisationProperties.getDestinationUrl());
    }

    private HttpResponse<JsonNode> getUserKey(String jiraUsername, String jiraUrl) {
        String jiraRetrieveUserKeyUrl = String.format("%srest/api/2/user/search?username=%s", jiraUrl, jiraUsername);

        return request.get(jiraRetrieveUserKeyUrl);
    }

    @PostConstruct
    private void setRequestAuthenticationMethod() {
        BasicAuth basicAuth = new BasicAuth()
                .setUsername(jiraSynchronisationProperties.getAdminUsername())
                .setPassword(jiraSynchronisationProperties.getAdminPassword());
        request.setAuthentication(basicAuth);
    }
}
