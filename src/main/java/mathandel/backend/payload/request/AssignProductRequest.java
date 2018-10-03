package mathandel.backend.payload.request;

public class AssignProductRequest {

    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public AssignProductRequest setProductId(Long productId) {
        this.productId = productId;
        return this;
    }
}
