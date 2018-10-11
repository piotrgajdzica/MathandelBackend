package mathandel.backend.service;

import mathandel.backend.model.ModeratorRequest;
import mathandel.backend.model.User;
import mathandel.backend.model.client.ModeratorRequestTO;
import mathandel.backend.model.client.UserTO;

public class ServerToClientDataConverter {
    public static ModeratorRequestTO mapModeratorRequest(ModeratorRequest moderatorRequest) {
                return new mathandel.backend.model.client.ModeratorRequestTO()
                        .setModeratorRequestStatus(String.valueOf(moderatorRequest.getModeratorRequestStatus().getName()))
                        .setReason(moderatorRequest.getReason())
                        .setUser(mapUserFull(moderatorRequest.getUser()));

    }

    private static UserTO mapUserFull(User user) {
        // todo merge
        return null;
    }

}
