package mathandel.backend.model.client;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class DefinedGroupTO {

    private Long id;
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;
    private Set<Long> itemsIds;
    private Set<Long> groupIds;

    public Long getId() {
        return id;
    }

    public DefinedGroupTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DefinedGroupTO setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Long> getItemsIds() {
        return itemsIds;
    }

    public DefinedGroupTO setItemsIds(Set<Long> itemsIds) {
        this.itemsIds = itemsIds;
        return this;
    }

    public Set<Long> getGroupIds() {
        return groupIds;
    }

    public DefinedGroupTO setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
        return this;
    }
}
