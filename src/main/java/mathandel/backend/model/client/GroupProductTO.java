package mathandel.backend.model.client;

import javax.validation.constraints.NotNull;

public class GroupProductTO {

    @NotNull
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public GroupProductTO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }
}
