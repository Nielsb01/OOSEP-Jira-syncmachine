package nl.avisi.http;

import java.net.URI;

public interface IRequest<AuthenticationMethod, Response> {
    void setAuthenticationMethod(AuthenticationMethod authenticationMethod);
    Response post(String url, String data);
    Response get(String url);
}
