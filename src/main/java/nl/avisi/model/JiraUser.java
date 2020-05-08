package nl.avisi.model;

import kong.unirest.*;

import kong.unirest.json.JSONException;
import nl.avisi.InvalidEmailException;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyReaders.JiraSynchronisationProperties;

import javax.inject.Inject;

public class JiraUser {

    private String clientUrl;

    private String avisiUrl;

    private IRequest request;

    private BasicAuth basicAuth;

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

    public String retrieveJiraUserKeyByEmail(String email, String server) {
        String jiraUserKey = "";

        setBasicAuth(new BasicAuth()
                .setPassword(jiraSynchronisationProperties.getAdminPassword())
                .setUsername(jiraSynchronisationProperties.getAdminUsername()));
        request.setAuthentication(basicAuth);

        String requestUrl = determineUrl(server) + email;

        HttpResponse<JsonNode> JSONJiraUser = request.get(requestUrl);

        try {
            jiraUserKey = JSONJiraUser.getBody().getArray().getJSONObject(0).getString("key");
        } catch (JSONException e) {
            throw new InvalidEmailException();
        }

        if (jiraUserKey.isEmpty()) {
            throw new InvalidEmailException();
        }

        return jiraUserKey;
    }

    private String determineUrl(String server) {
        return server.equalsIgnoreCase("Avisi") ? avisiUrl : clientUrl;
    }
}
