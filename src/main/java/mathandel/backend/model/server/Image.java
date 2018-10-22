package mathandel.backend.model.server;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Image image = (Image) o;

        return new EqualsBuilder()
                .append(id, image.id)
                .append(name, image.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .toHashCode();
    }
}
