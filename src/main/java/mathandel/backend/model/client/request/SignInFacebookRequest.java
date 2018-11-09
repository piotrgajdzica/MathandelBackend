package mathandel.backend.model.client.request;

import javax.validation.constraints.NotBlank;

public class SignInFacebookRequest {

    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public SignInFacebookRequest setToken(String token) {
        this.token = token;
        return this;
    }
}
