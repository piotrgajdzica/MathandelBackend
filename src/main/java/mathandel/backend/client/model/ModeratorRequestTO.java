package mathandel.backend.client.model;

public class ModeratorRequestTO {
    private UserTO user;

    public Long getUserId() {
        return userId;
    }

    public ModeratorRequestTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    private Long userId;
    private String reason;
    private String moderatorRequestStatus;

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

    public String getModeratorRequestStatus() {
        return moderatorRequestStatus;
    }

    public ModeratorRequestTO setModeratorRequestStatus(String moderatorRequestStatus) {
        this.moderatorRequestStatus = moderatorRequestStatus;
        return this;
    }
}
