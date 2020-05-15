package nl.avisi.network;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

public interface IRequest<T> {
    void setAuthentication(T authentication);

    HttpResponse<JsonNode> get(String url);

    <S> HttpResponse<JsonNode> post(String url, S data);
}
