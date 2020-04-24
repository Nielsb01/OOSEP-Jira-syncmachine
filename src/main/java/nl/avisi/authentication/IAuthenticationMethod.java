package nl.avisi.authentication;

public interface IAuthenticationMethod<HttpRequest, Credentials> {
    void setAuthenticationDetails(HttpRequest request, Credentials credentials);
}
