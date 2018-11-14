package mathandel.backend.model.client;

import java.util.HashSet;
import java.util.Set;

public class ModeratorRequestsTO {

    private Set<ModeratorRequestTO> moderatorRequests = new HashSet<>();

    public Set<ModeratorRequestTO> getModeratorRequests() {
        return moderatorRequests;
    }

    public ModeratorRequestsTO setModeratorRequestsTO(Set<ModeratorRequestTO> moderatorRequestsTO) {
        this.moderatorRequests = moderatorRequestsTO;
        return this;
    }

}
