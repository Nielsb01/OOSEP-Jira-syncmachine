package nl.avisi.network;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import nl.avisi.network.authentication.BasicAuth;


/**
 * Send a request with the basic auth headers
 * The basic auth credentials are set using
 * the constructor
 */
public class BasicAuthRequest implements IRequest<BasicAuth> {

    /**
     * Header name for the content type which is
     * expected by the Unirest client
     */
    private static final String acceptHeader = "Accept";

    /**
     * Header name for the content type which is
     * sent by the Unirest client
     */
    private static final String contentTypeHeader = "Content-Type";

    /**
     * The type of data that is accepted by the Unirest
     * client
     */
    private static final String contentTypeJson = "application/json";

    /**
     * The basic auth information to send
     * with the request
     */
    private BasicAuth authentication;

    /**
     * Set the basic auth information
     *
     * @param authentication basic auth
     */
    public void setAuthentication(BasicAuth authentication) {
        this.authentication = authentication;
    }

    /**
     * Send a get request to the given url
     *
     * @param url the endpoint to send a get request to
     * @return the response of the get request
     */
    public HttpResponse<JsonNode> get(String url) {
        return Unirest.get(url)
                .basicAuth(authentication.getUsername(), authentication.getPassword())
                .header(acceptHeader, contentTypeJson)
                .asJson();
    }

    /**
     * Post the object to the given url
     *
     * @param url  the endpoint to send the json object to
     * @param data the object to be sent
     * @return the response of the post request
     */
    public <T> HttpResponse<JsonNode> post(String url, T data) {
        return Unirest.post(url)
                .basicAuth(authentication.getUsername(), authentication.getPassword())
                .header(acceptHeader, contentTypeJson)
                .header(contentTypeHeader, contentTypeJson)
                .body(data)
                .asJson();
    }
}
