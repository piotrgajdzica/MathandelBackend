package mathandel.backend.payload.response;

import mathandel.backend.model.ModeratorRequest;

import java.util.List;

public class ModeratorRequestsResponse extends ApiResponse{
    List<ModeratorRequest> moderatorRequests;

    public ModeratorRequestsResponse(Boolean success, String message) {
        super(success, message);
    }

    public List<ModeratorRequest> getModeratorRequests() {
        return moderatorRequests;
    }

    public ModeratorRequestsResponse setModeratorRequests(List<ModeratorRequest> moderatorRequests) {
        this.moderatorRequests = moderatorRequests;
        return this;
    }
}
