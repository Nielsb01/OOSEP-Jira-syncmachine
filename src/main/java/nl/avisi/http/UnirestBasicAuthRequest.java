package nl.avisi.http;

import kong.unirest.*;
import nl.avisi.authentication.UnirestBasicAuthenticationMethod;
import nl.avisi.authentication.credentials.BasicAuthCredentials;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.net.URI;

public class UnirestBasicAuthRequest implements IRequest<UnirestBasicAuthenticationMethod, JsonNode> {

    @NotNull
    private UnirestBasicAuthenticationMethod authenticationMethod;

    @Inject
    public void setAuthenticationMethod(@NotNull UnirestBasicAuthenticationMethod authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public JsonNode post(String url, String data) {
        final HttpRequestWithBody request = Unirest.post(url.toString());
        this.addCredentialsToRequest(request, "", "");
        request.body(data);

        return request.asJson().getBody();
    }

    public JsonNode get(String url) {
        final GetRequest request = Unirest.get(url.toString());
        this.addCredentialsToRequest(request, "", "");

        return request.asJson().getBody();
    }

    private void addCredentialsToRequest(@NotNull final HttpRequest request, @NotNull final String emailAddress, final String password) {
        final BasicAuthCredentials credentials = new BasicAuthCredentials(emailAddress, password);
        this.authenticationMethod.setAuthenticationDetails(request, credentials);
    }
}
