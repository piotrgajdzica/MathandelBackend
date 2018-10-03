package mathandel.backend.payload.response;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long userId;

    public Long getId() {
        return id;
    }

    public ProductResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public ProductResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
