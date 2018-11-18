package mathandel.backend.model.server;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @NaturalId
    @OneToOne
    private Product haveProduct;

    @ManyToMany
    private Set<Product> wantedProducts = new HashSet<>();

    @ManyToMany
    private Set<DefinedGroup> wantedDefinedGroups = new HashSet<>();

    @ManyToOne
    private Edition edition;

    public Set<DefinedGroup> getWantedDefinedGroups() {
        return wantedDefinedGroups;
    }

    public Set<Product> getWantedProducts() {
        return wantedProducts;
    }

    public Preference setWantedProducts(Set<Product> wantedProducts) {
        this.wantedProducts = wantedProducts;
        return this;
    }

    public Preference setWantedDefinedGroups(Set<DefinedGroup> wantedDefinedGroups) {
        this.wantedDefinedGroups = wantedDefinedGroups;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Preference setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Preference setUser(User user) {
        this.user = user;
        return this;
    }

    public Product getHaveProduct() {
        return haveProduct;
    }

    public Preference setHaveProduct(Product haveProduct) {
        this.haveProduct = haveProduct;
        return this;
    }

    public Edition getEdition() {
        return edition;
    }

    public Preference setEdition(Edition edition) {
        this.edition = edition;
        return this;
    }
}

