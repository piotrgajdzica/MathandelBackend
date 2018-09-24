package mathandel.backend.payload.request;

import mathandel.backend.model.ModeratorRequestStatus;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ModeratorRequestStatusChangeRequest {
    @NotBlank
    private List<Long> userIds;

    private ModeratorRequestStatus moderatorRequestStatus;

    public List<Long> getUserIds() {
        return userIds;
    }

    public ModeratorRequestStatusChangeRequest setUserIds(List<Long> userIds) {
        this.userIds = userIds;
        return this;
    }

    public ModeratorRequestStatus getModeratorRequestStatus() {
        return moderatorRequestStatus;
    }

    public ModeratorRequestStatusChangeRequest setModeratorRequestStatus(ModeratorRequestStatus moderatorRequestStatus) {
        this.moderatorRequestStatus = moderatorRequestStatus;
        return this;
    }
}
