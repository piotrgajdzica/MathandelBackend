package mathandel.backend.payload.request;

import mathandel.backend.model.ModeratorRequestStatus;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ModeratorRequestStatusChangeRequest {
    @NotBlank
    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public ModeratorRequestStatusChangeRequest setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
