package mathandel.backend.model.client;

import java.util.HashSet;
import java.util.Set;

public class ModeratorRequestsTO {

    private Set<ModeratorRequestTO> moderatorRequestTO = new HashSet<>();

    public Set<ModeratorRequestTO> getModeratorRequests() {
        return moderatorRequestTO;
    }

    public ModeratorRequestsTO setModeratorRequestsTO(Set<ModeratorRequestTO> moderatorRequestsTO) {
        this.moderatorRequestTO = moderatorRequestsTO;
        return this;
    }

}
