package mathandel.backend.payload.request;

import mathandel.backend.model.ModeratorRequestStatus;

import javax.validation.constraints.NotBlank;

public class ModeratorRequestStatusChangeRequest {
    @NotBlank
    private Long userId;

    private ModeratorRequestStatus moderatorRequestStatus;

    public Long getUserId() {
        return userId;
    }

    public ModeratorRequestStatusChangeRequest setUserId(Long userId) {
        this.userId = userId;
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
