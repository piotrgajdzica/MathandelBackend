package mathandel.backend.payload.request;

public class CreateEditProductRequest {

    private String name;
    private String description;

    public CreateEditProductRequest() {
    }

    public String getName() {
        return name;
    }

    public CreateEditProductRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateEditProductRequest setDescription(String description) {
        this.description = description;
        return this;
    }
}
