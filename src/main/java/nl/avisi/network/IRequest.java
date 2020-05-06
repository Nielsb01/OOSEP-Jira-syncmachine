package nl.avisi.network;

import kong.unirest.JsonNode;

public interface IRequest<Authentication> {
    void setAuthentication(Authentication authentication);
    JsonNode get(String url);
    <PostData> JsonNode post(String url, PostData data);
}
