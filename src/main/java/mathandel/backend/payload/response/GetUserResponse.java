package mathandel.backend.payload.response;

import mathandel.backend.model.Role;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class GetUserResponse {

    private String name;
    private String surname;
    private String username;
    private String email;
    private Set<Role> roles;

    public String getName() {
        return name;
    }

    public GetUserResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public GetUserResponse setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GetUserResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GetUserResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public GetUserResponse setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        GetUserResponse that = (GetUserResponse) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(surname, that.surname)
                .append(username, that.username)
                .append(email, that.email)
                .append(roles, that.roles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(surname)
                .append(username)
                .append(email)
                .append(roles)
                .toHashCode();
    }
}
