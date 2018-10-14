package mathandel.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "preferences")
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private User user;

    @ManyToOne
    private Product haveProduct;

    @ManyToOne
    private Product wantProduct;

    @ManyToOne
    private DefinedGroup definedGroup;

    @Column(name = "editionId")
    private Edition edition;


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

    public Product getWantProduct() {
        return wantProduct;
    }

    public Preference setWantProduct(Product wantProduct) {
        this.wantProduct = wantProduct;
        return this;
    }

    public DefinedGroup getDefinedGroup() {
        return definedGroup;
    }

    public Preference setDefinedGroup(DefinedGroup definedGroup) {
        this.definedGroup = definedGroup;
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

