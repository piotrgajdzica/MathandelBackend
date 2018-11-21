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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @ManyToOne
    private Edition edition;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Item> items = new HashSet<>();

    @OneToMany
    private Set<DefinedGroup> groups = new HashSet<>();

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

    public Set<Item> getItems() {
        return items;
    }

    public DefinedGroup setItems(Set<Item> items) {
        this.items = items;
        return this;
    }

    public Set<DefinedGroup> getGroups() {
        return groups;
    }

    public DefinedGroup setGroups(Set<DefinedGroup> groups) {
        this.groups = groups;
        return this;
    }
}
