package mathandel.backend.model.client;

import java.util.Set;

public class PreferenceTO {

    private Long id;

    private Long userId;

    private Long haveProduct;

    private Set<Long> wantedProducts;

    private Set<Long> wantedDefinedGroups;

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

    public Long getHaveProduct() {
        return haveProduct;
    }

    public PreferenceTO setHaveProductId(Long haveProduct) {
        this.haveProduct = haveProduct;
        return this;
    }

    public Set<Long> getWantedProducts() {
        return wantedProducts;
    }

    public PreferenceTO setWantedProducts(Set<Long> wantedProducts) {
        this.wantedProducts = wantedProducts;
        return this;
    }

    public Set<Long> getWantedDefinedGroups() {
        return wantedDefinedGroups;
    }

    public PreferenceTO setWantedDefinedGroups(Set<Long> wantedDefinedGroups) {
        this.wantedDefinedGroups = wantedDefinedGroups;
        return this;
    }



}
