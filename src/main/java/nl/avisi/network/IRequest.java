package nl.avisi.network;

import com.google.gson.JsonObject;
import kong.unirest.JsonNode;

public interface IRequest {
    JsonNode get(String url);
    JsonNode post(String url, JsonObject data);
}
