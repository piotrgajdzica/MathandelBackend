package mathandel.backend.model.client;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserTO {

    private Long id;

    private Boolean userExists = true;

    @NotBlank
    @Size(min = 2, max = 40)
    private String name;

    @NotBlank
    @Size(min = 2, max = 40)
    private String surname;

    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public UserTO setId(Long id) {
        this.id = id;
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

    public Set<String> getRoles() {
        return roles;
    }

    public UserTO setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserTO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public UserTO setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserTO setCountry(String country) {
        this.country = country;
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
                .append(id, userTO.id)
                .append(name, userTO.name)
                .append(surname, userTO.surname)
                .append(username, userTO.username)
                .append(email, userTO.email)
                .append(roles, userTO.roles)
                .isEquals();
    }

    public String getAddress() {
        return address;
    }

    public UserTO setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(surname)
                .append(username)
                .append(email)
                .append(roles)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("surname", surname)
                .append("username", username)
                .append("email", email)
                .append("roles", roles)
                .toString();
    }

    public Boolean getUserExists() {
        return userExists;
    }
}
