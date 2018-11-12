package mathandel.backend.model.client;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class PreferenceTO {

    private Long id;
    private Long userId;
    private Long haveProductId;
    @NotNull
    private Set<Long> wantedProductsIds;
    @NotNull
    private Set<Long> wantedDefinedGroupsIds;

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

    public Long getHaveProductId() {
        return haveProductId;
    }

    public PreferenceTO setHaveProductId(Long haveProduct) {
        this.haveProductId = haveProduct;
        return this;
    }

    public Set<Long> getWantedProductsIds() {
        return wantedProductsIds;
    }

    public PreferenceTO setWantedProductsIds(Set<Long> wantedProductsIds) {
        this.wantedProductsIds = wantedProductsIds;
        return this;
    }

    public Set<Long> getWantedDefinedGroupsIds() {
        return wantedDefinedGroupsIds;
    }

    public PreferenceTO setWantedDefinedGroupsIds(Set<Long> wantedDefinedGroupsIds) {
        this.wantedDefinedGroupsIds = wantedDefinedGroupsIds;
        return this;
    }



}
