package mathandel.backend.client.request;

public class ProductDataRequest {

    private String name;
    private String description;

    public ProductDataRequest() {
    }

    public String getName() {
        return name;
    }

    public ProductDataRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDataRequest setDescription(String description) {
        this.description = description;
        return this;
    }
}
