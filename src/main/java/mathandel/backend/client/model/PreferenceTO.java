package mathandel.backend.client.model;

public class PreferenceTO {
    private Long id;

    private Long userId;

    private ProductTO haveProductId;

    private ProductTO wantProductId;

    private DefinedGroupTO definedGroupId;

    public Long getId() {
        return id;
    }

    public PreferenceTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public PreferenceTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public ProductTO getHaveProductId() {
        return haveProductId;
    }

    public PreferenceTO setHaveProductId(ProductTO haveProductId) {
        this.haveProductId = haveProductId;
        return this;
    }

    public ProductTO getWantProductId() {
        return wantProductId;
    }

    public PreferenceTO setWantProductId(ProductTO wantProductId) {
        this.wantProductId = wantProductId;
        return this;
    }

    public DefinedGroupTO getDefinedGroupId() {
        return definedGroupId;
    }

    public PreferenceTO setDefinedGroupId(DefinedGroupTO definedGroupId) {
        this.definedGroupId = definedGroupId;
        return this;
    }
}
