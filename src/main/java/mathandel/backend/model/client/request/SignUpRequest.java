package mathandel.backend.model.client.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    private String name;

    @NotBlank
    @Size(min = 2, max = 40)
    private String surname;

    @NotBlank
    @Size(min = 2, max = 40)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;

    public String getName() {
        return name;
    }

    public SignUpRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public SignUpRequest setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SignUpRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SignUpRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SignUpRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public SignUpRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public SignUpRequest setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public SignUpRequest setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public SignUpRequest setCountry(String country) {
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

        SignUpRequest that = (SignUpRequest) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(surname, that.surname)
                .append(username, that.username)
                .append(email, that.email)
                .append(password, that.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(surname)
                .append(username)
                .append(email)
                .append(password)
                .toHashCode();
    }
}
