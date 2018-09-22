package mathandel.backend.payload.request;

public class ModeratorRequestReasonRequest {
    private String reason;

    public String getReason() {
        return reason;
    }

    public ModeratorRequestReasonRequest setReason(String reason) {
        this.reason = reason;
        return this;
    }
}
