package mathandel.backend.model.server;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Edition edition;

    @OneToMany
    private Set<Image> images = new HashSet<>();

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

    public Set<Image> getImages() {
        return images;
    }

    public Product setImages(Set<Image> images) {
        this.images = images;
        return this;
    }
}
