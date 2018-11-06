package mathandel.backend.model.client;

public class ImageTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public ImageTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ImageTO setName(String name) {
        this.name = name;
        return this;
    }
}
