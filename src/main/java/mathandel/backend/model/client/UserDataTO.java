package mathandel.backend.model.client;

import java.util.Set;

public class UserDataTO {

    private String username;
    private String email;
    private String name;
    private String surname;
    private Set<RateTO> rates;

    public String getUsername() {
        return username;
    }

    public UserDataTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDataTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDataTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserDataTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Set<RateTO> getRates() {
        return rates;
    }

    public UserDataTO setRates(Set<RateTO> rates) {
        this.rates = rates;
        return this;
    }
}
