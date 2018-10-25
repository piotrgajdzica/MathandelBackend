package mathandel.backend.model.client;

import java.util.Set;

public class DefinedGroupContentTO {

    private Set<Long> productsIds;

    private Set<Long> groupIds;

    public Set<Long> getProductsIds() {
        return productsIds;
    }

    public DefinedGroupContentTO setProductsIds(Set<Long> productsIds) {
        this.productsIds = productsIds;
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
