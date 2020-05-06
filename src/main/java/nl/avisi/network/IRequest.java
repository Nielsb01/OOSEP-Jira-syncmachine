package nl.avisi.network;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface IRequest<Authentication> {
    void setAuthentication(Authentication authentication);
    HttpResponse<JsonNode> get(String url);
    <PostData> HttpResponse<JsonNode> post(String url, PostData data);
}
