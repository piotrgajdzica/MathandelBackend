package mathandel.backend.model.client;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ProductTO {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Long userId;
    private Long editionId;
    @NotNull
    private Set<ImageTO> images;

    public Long getId() {
        return id;
    }

    public ProductTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public ProductTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Set<ImageTO> getImages() {
        return images;
    }

    public ProductTO setImages(Set<ImageTO> images) {
        this.images = images;
        return this;
    }

    public Long getEditionId() {
        return editionId;
    }

    public ProductTO setEditionId(Long editionId) {
        this.editionId = editionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductTO product = (ProductTO) o;

        return new EqualsBuilder()
                .append(id, product.id)
                .append(name, product.name)
                .append(description, product.description)
                .append(userId, product.userId)
                .append(editionId, product.editionId)
                .append(images, product.images)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(description)
                .append(userId)
                .append(editionId)
                .append(images)
                .toHashCode();
    }
}
