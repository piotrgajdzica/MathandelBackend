package mathandel.backend.model.client;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class PreferenceTO {

    private Long id;
    private Long userId;
    private Long haveItemId;
    @NotNull
    private Set<Long> wantedItemsIds;
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

    public Long getHaveItemId() {
        return haveItemId;
    }

    public PreferenceTO setHaveItemId(Long haveItemId) {
        this.haveItemId = haveItemId;
        return this;
    }

    public Set<Long> getWantedItemsIds() {
        return wantedItemsIds;
    }

    public PreferenceTO setWantedItemsIds(Set<Long> wantedItemsIds) {
        this.wantedItemsIds = wantedItemsIds;
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
