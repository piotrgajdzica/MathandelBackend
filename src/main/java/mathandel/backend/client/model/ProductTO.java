package mathandel.backend.client.model;

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
}
