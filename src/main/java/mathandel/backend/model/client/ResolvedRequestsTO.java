package mathandel.backend.model.client;

import java.util.Set;

public class ResolvedRequestsTO {

    private Set<Long> acceptedRequestsIds;
    private Set<Long> rejectedRequestsIds;

    public Set<Long> getAcceptedRequestsIds() {
        return acceptedRequestsIds;
    }

    public ResolvedRequestsTO setAcceptedRequestsIds(Set<Long> acceptedRequestsIds) {
        this.acceptedRequestsIds = acceptedRequestsIds;
        return this;
    }

    public Set<Long> getRejectedRequestsIds() {
        return rejectedRequestsIds;
    }

    public ResolvedRequestsTO setRejectedRequestsIds(Set<Long> rejectedRequestsIds) {
        this.rejectedRequestsIds = rejectedRequestsIds;
        return this;
    }
}
