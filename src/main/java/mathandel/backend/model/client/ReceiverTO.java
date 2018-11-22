package mathandel.backend.model.client;

public class ReceiverTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String address;
    private String city;
    private String country;
    private String postalCode;

    public Long getId() {
        return id;
    }

    public ReceiverTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ReceiverTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public ReceiverTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ReceiverTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ReceiverTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ReceiverTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ReceiverTO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public ReceiverTO setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public ReceiverTO setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }
}
