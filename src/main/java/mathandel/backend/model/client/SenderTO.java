package mathandel.backend.model.client;

public class SenderTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;

    public Long getId() {
        return id;
    }

    public SenderTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SenderTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public SenderTO setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SenderTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SenderTO setEmail(String email) {
        this.email = email;
        return this;
    }
}
