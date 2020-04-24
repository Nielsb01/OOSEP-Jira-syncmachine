package nl.avisi.authentication;

import kong.unirest.HttpRequest;
import nl.avisi.authentication.credentials.BasicAuthCredentials;
import nl.avisi.service.UnirestRequest;

/**
 * Basic Authentication method for a request
 *
 */
public class UnirestBasicAuthenticationMethod implements IAuthenticationMethod<> {

    /**
     * Set the basic auth credentials on the unirest request
     *
     * @param httpClient the unirest request used to eventually send the request
     * @param credentials the credentials to login using basic authentication
     */
    public <HttpClient extends HttpRequest, Credentials extends BasicAuthCredentials> void setAuthenticationDetails(HttpClient httpClient, Credentials credentials) {
        httpClient.basicAuth(credentials.getEmailAddress(), credentials.getPassword());
    }

    public <HttpClient, Credentials> void setAuthenticationDetails(HttpClient httpClient, Credentials credentials) {

    }

    public void setAuthenticationDetails(Object o, Object o2) {

    }
}
