package mathandel.backend.model.client.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class JwtAuthenticationResponse {

    private Boolean userExists = true;
    private String accessToken;
    private Set<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public JwtAuthenticationResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public JwtAuthenticationResponse setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public Boolean getUserExists() {
        return userExists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JwtAuthenticationResponse that = (JwtAuthenticationResponse) o;

        return new EqualsBuilder()
                .append(accessToken, that.accessToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(accessToken)
                .toHashCode();
    }
}
