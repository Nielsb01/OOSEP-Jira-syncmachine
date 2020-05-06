package nl.avisi.network;

import com.google.gson.JsonObject;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.http.entity.mime.Header;

/**
 * Send a request with the basic auth headers
 *
 * The basic auth credentials are set using
 * the constructor
 */
public class BasicAuthRequest implements IRequest {

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
     * Username used for basic auth
     */
    private String username;

    /**
     * Password used for basic auth
     */
    private String password;

    public BasicAuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Send a get request to the give url
     *
     * @param url the eddpoint to send a get request to
     * @return the response of the get request
     */
    public JsonNode get(String url) {
        return Unirest.get(url)
                .basicAuth(this.username, this.password)
                .header(acceptHeader, contentTypeJson)
                .header(contentTypeHeader, contentTypeJson)
                .asJson()
                .getBody();
    }

    /**
     * Post the json object to the given url
     *
     * @param url the endpoint to send the json object to
     * @param data the json object to be sent
     * @return the response of the post request
     */
    public JsonNode post(String url, JsonObject data) {
        return Unirest.post(url)
                .basicAuth(this.username, this.password)
                .header(acceptHeader, contentTypeJson)
                .header(contentTypeHeader, contentTypeJson)
                .body(data)
                .asJson()
                .getBody();
    }
}
