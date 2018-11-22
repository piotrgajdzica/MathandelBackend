package mathandel.backend.model.client.request;

import java.util.Set;

//todo rename
public class CreateUpdateItemRequest {
    private String name;
    private String description;
    private Set<Long> imagesToRemove;

    public String getName() {
        return name;
    }

    public CreateUpdateItemRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateUpdateItemRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<Long> getImagesToRemove() {
        return imagesToRemove;
    }

    public CreateUpdateItemRequest setImagesToRemove(Set<Long> imagesToRemove) {
        this.imagesToRemove = imagesToRemove;
        return this;
    }
}
