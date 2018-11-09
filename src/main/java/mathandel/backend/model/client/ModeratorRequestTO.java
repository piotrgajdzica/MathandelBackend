package mathandel.backend.model.client;

import mathandel.backend.model.server.ModeratorRequestStatusName;

public class ModeratorRequestTO {
    private Long id;
    private Long userId;
    private String reason;
    private ModeratorRequestStatusName moderatorRequestStatus;

    public Long getId() {
        return id;
    }

    public ModeratorRequestTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public ModeratorRequestTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ModeratorRequestTO setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ModeratorRequestStatusName getModeratorRequestStatus() {
        return moderatorRequestStatus;
    }

    public ModeratorRequestTO setModeratorRequestStatus(ModeratorRequestStatusName moderatorRequestStatus) {
        this.moderatorRequestStatus = moderatorRequestStatus;
        return this;
    }
}
