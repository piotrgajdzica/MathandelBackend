package mathandel.backend.payload.response;

import mathandel.backend.model.Role;

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
}
