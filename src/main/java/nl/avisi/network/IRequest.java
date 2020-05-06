package nl.avisi.network;

import kong.unirest.JsonNode;

public interface IRequest<Authentication, PostData> {
    void setAuthentication(Authentication authentication);
    JsonNode get(String url);
    JsonNode post(String url, PostData data);
}
