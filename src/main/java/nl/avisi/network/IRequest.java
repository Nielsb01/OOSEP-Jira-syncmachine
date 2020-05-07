package nl.avisi.network;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface IRequest<AUTHENTICATION> {
    void setAuthentication(AUTHENTICATION authentication);
    HttpResponse<JsonNode> get(String url);
    <DATA> HttpResponse<JsonNode> post(String url, DATA data);
}
