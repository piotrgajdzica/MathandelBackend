package mathandel.backend.model.client;

import mathandel.backend.model.server.ModeratorRequestStatusName;

public class ModeratorRequestTO {
    //todo id of request
    //todo only ids
    private UserTO user;
    private Long userId;
    private String reason;
    //todo as enum
    private ModeratorRequestStatusName moderatorRequestStatus;

    public Long getUserId() {
        return userId;
    }

    public ModeratorRequestTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserTO getUser() {
        return user;
    }

    public ModeratorRequestTO setUser(UserTO user) {
        this.user = user;
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
