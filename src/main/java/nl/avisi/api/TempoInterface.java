package nl.avisi.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import nl.avisi.dto.DestinationWorklogDTO;
import nl.avisi.dto.WorklogRequestDTO;
import nl.avisi.network.IRequest;
import nl.avisi.network.authentication.BasicAuth;
import nl.avisi.propertyreaders.JiraSynchronisationProperties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class TempoInterface {

    public static final String REST_TEMPO_WORKLOGS_CREATE = "rest/tempo-timesheets/4/worklogs";
    public static final String REST_TEMPO_WORKLOGS_REQUEST = "rest/tempo-timesheets/4/worklogs/search";

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
     * Sends the actual HTTP post request for the worklogs to the Tempo API
     *
     * @param requestBody Contains the parameters to specify the worklogs to be retrieved during the HTTP request.
     * @return httpResponse containing the worklogs in JsonNode form.
     */
    public HttpResponse<JsonNode> requestOriginJiraWorklogs(WorklogRequestDTO requestBody) {
        return request.post(jiraSynchronisationProperties.getOriginUrl() + REST_TEMPO_WORKLOGS_REQUEST, requestBody);
    }

    /**
     * Creates a worklog on the destination server
     * @param worklog The worklog to be created
     * @return The response given by tempo
     */
    public HttpResponse<JsonNode> createWorklogOnDestinationServer(DestinationWorklogDTO worklog) {
        return request.post(jiraSynchronisationProperties.getDestinationUrl() + REST_TEMPO_WORKLOGS_CREATE, worklog);
    }

    @PostConstruct
    private void setRequestAuthenticationMethod() {
        BasicAuth basicAuth = new BasicAuth()
                .setUsername(jiraSynchronisationProperties.getAdminUsername())
                .setPassword(jiraSynchronisationProperties.getAdminPassword());
        request.setAuthentication(basicAuth);
    }
}
