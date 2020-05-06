package nl.avisi.network;

import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import nl.avisi.network.authentication.BasicAuth;

/**
 * Send a request with the basic auth headers
 *
 * The basic auth credentials are set using
 * the constructor
 */
public class BasicAuthRequest<PostData> implements IRequest<BasicAuth, PostData> {

    /**
     * Header name for the content type which is
     * expected by the Unirest client
     */
    private final String acceptHeader = "Accept";

    /**
     * Header name for the content type which is
     * sent by the Unirest client
     */
    private final String contentTypeHeader = "Content-Type";

    /**
     * The type of data that is accepted by the Unirest
     * client
     */
    private final String contentTypeJson = "application/json";

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
     * Send a get request to the give url
     *
     * @param url the eddpoint to send a get request to
     * @return the response of the get request
     */
    public JsonNode get(String url) {
        return Unirest.get(url)
                .basicAuth(authentication.getUsername(), authentication.getPassword())
                .header(acceptHeader, contentTypeJson)
                .asJson()
                .getBody();
    }

    /**
     * Post the object to the given url
     *
     * @param url the endpoint to send the json object to
     * @param data the object to be sent
     * @return the response of the post request
     */
    public JsonNode post(String url, PostData data) {
        return Unirest.post(url)
                .basicAuth(authentication.getUsername(), authentication.getPassword())
                .header(acceptHeader, contentTypeJson)
                .header(contentTypeHeader, contentTypeJson)
                .body(data)
                .asJson()
                .getBody();
    }
}
