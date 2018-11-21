package mathandel.backend.model.client;

import java.util.Set;

public class DefinedGroupContentTO {

    private Set<Long> itemsIds;
    private Set<Long> groupIds;

    public Set<Long> getItemsIds() {
        return itemsIds;
    }

    public DefinedGroupContentTO setItemsIds(Set<Long> itemsIds) {
        this.itemsIds = itemsIds;
        return this;
    }

    public Set<Long> getGroupIds() {
        return groupIds;
    }

    public DefinedGroupContentTO setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
        return this;
    }
}
