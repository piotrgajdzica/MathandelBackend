package mathandel.backend.model.server;

import javax.persistence.*;

//todo add list of urls with images
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Edition edition;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Product setUser(User user) {
        this.user = user;
        return this;
    }

    public Edition getEdition() {
        return edition;
    }

    public Product setEdition(Edition edition) {
        this.edition = edition;
        return this;
    }
}
