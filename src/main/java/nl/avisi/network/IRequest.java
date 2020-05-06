package nl.avisi.network;

import com.google.gson.JsonObject;
import kong.unirest.JsonNode;

public interface IRequest<Authentication> {
    void setAuthentication(Authentication authentication);
    JsonNode get(String url);
    JsonNode post(String url, JsonObject data);
}
