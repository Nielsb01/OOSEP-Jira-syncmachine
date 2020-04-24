package nl.avisi.service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import nl.avisi.dto.LoginDto;
import nl.avisi.exception.InvalidCredentialsException;

import java.util.*;

public class OAuthService implements AuthenticationService {
    private final String signatureMethod = "RSA-SHA1";

    private final String baseUri = "http://localhost";
    private final String requestToken = "/plugins/servlet/oauth/request-token";
    private final String authorize = "/plugins/servlet/oauth/authorize";

    public String login(LoginDto loginCredentials) throws InvalidCredentialsException {

        final Date date = Calendar.getInstance().getTime();
        final long millis = date.getTime();
        final long epoch = millis / 1000;
        final String consumerKey = "example";

        final Map<String, Object> oauthFields = new HashMap<String, Object>();
        oauthFields.put("oauth_consumer_key", consumerKey); // Consumer key

        // jira_privatekey.pem
        oauthFields.put("oauth_private_key", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGM0rEWmiGxPHdUSI+0MybQaVKHRS4JWDwFrdrRrzOoHxvFqeCCuy+YxC+RyU2ecRouslycCAYAnzfFAPRVQ6ia+4vvC5JksDuihc7YxixMfdpBaZOrMc3ujE6MJnrhej7OX06JOB+kokn26m2u9UauXfGQ/V672Tk5EX27GFX/QIDAQAB"); // Private key
        oauthFields.put("oauth_signature_method", signatureMethod);
        oauthFields.put("oauth_timestamp", epoch);
        oauthFields.put("oauth_nonce", "");
        oauthFields.put("oauth_signature", Arrays.toString(Base64.getEncoder().encode(consumerKey.getBytes())));

        HttpResponse<String> result = Unirest.post(String.format("%s%s", baseUri, requestToken))
                .header("X-Atlassian-Token", "no-check")
                .fields(oauthFields)
                .asString();

        return result.getBody();
    }
}
