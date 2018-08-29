package mathandel.backend.payload.response;

import mathandel.backend.model.Edition;
import mathandel.backend.model.Role;

import java.util.Set;

public class GetMeResponse {

    private String name;
    private String surname;
    private String username;
    private String email;
    private Set<Role> roles;
    private Set<Edition> editions;

    public String getName() {
        return name;
    }

    public GetMeResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public GetMeResponse setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GetMeResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GetMeResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public GetMeResponse setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Set<Edition> getEditions() {
        return editions;
    }

    public GetMeResponse setEditions(Set<Edition> editions) {
        this.editions = editions;
        return this;
    }
}
