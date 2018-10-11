package mathandel.backend.utils;

import mathandel.backend.client.model.UserTO;
import mathandel.backend.model.ModeratorRequest;
import mathandel.backend.model.User;
import mathandel.backend.client.model.ModeratorRequestTO;

public class ServerToClientDataConverter {
    public static ModeratorRequestTO mapModeratorRequest(ModeratorRequest moderatorRequest) {
                return new ModeratorRequestTO()
                        .setModeratorRequestStatus(String.valueOf(moderatorRequest.getModeratorRequestStatus().getName()))
                        .setReason(moderatorRequest.getReason())
                        .setUser(mapUserFull(moderatorRequest.getUser()));

    }

    private static UserTO mapUserFull(User user) {
        // todo merge
        return null;
    }

}
