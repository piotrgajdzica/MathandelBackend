package mathandel.backend.model.server;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "defined_groups")
public class DefinedGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Edition edition;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    public DefinedGroup() {
    }

    public Long getId() {
        return id;
    }

    public DefinedGroup setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DefinedGroup setName(String name) {
        this.name = name;
        return this;
    }

    public Edition getEdition() {
        return edition;
    }

    public DefinedGroup setEdition(Edition edition) {
        this.edition = edition;
        return this;
    }

    public User getUser() {
        return user;
    }

    public DefinedGroup setUser(User user) {
        this.user = user;
        return this;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public DefinedGroup setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }
}
