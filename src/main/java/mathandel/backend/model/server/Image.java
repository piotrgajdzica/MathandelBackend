package mathandel.backend.model.server;

import javax.persistence.*;

@Entity
@Table(name = "images", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public Image() {
    }

    public Long getId() {
        return id;
    }

    public Image setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Image setName(String name) {
        this.name = name;
        return this;
    }
}
