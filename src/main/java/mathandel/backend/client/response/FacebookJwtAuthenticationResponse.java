package mathandel.backend.client.response;

public class FacebookJwtAuthenticationResponse extends FacebookResponse {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public FacebookJwtAuthenticationResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
