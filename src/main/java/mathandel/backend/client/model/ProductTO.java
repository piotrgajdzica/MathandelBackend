package mathandel.backend.client.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ProductTO {

    private Long id;
    private String name;
    private String description;
    private Long userId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductTO productTO = (ProductTO) o;

        return new EqualsBuilder()
                .append(id, productTO.id)
                .append(name, productTO.name)
                .append(description, productTO.description)
                .append(userId, productTO.userId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(description)
                .append(userId)
                .toHashCode();
    }
}
