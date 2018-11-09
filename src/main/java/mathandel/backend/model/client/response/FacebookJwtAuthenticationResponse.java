package mathandel.backend.model.client.response;

import java.util.Set;

public class FacebookJwtAuthenticationResponse extends FacebookResponse {

    private String accessToken;
    private Set<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public FacebookJwtAuthenticationResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public FacebookJwtAuthenticationResponse setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
}
