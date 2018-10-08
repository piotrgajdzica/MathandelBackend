package mathandel.backend.client.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class UserTO {

    private Long Id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Set<RoleTO> roles;

    public Long getId() {
        return Id;
    }

    public UserTO setId(Long id) {
        Id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<RoleTO> getRoles() {
        return roles;
    }

    public UserTO setRoles(Set<RoleTO> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserTO userTO = (UserTO) o;

        return new EqualsBuilder()
                .append(Id, userTO.Id)
                .append(name, userTO.name)
                .append(surname, userTO.surname)
                .append(username, userTO.username)
                .append(email, userTO.email)
                .append(roles, userTO.roles)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(Id)
                .append(name)
                .append(surname)
                .append(username)
                .append(email)
                .append(roles)
                .toHashCode();
    }
}
