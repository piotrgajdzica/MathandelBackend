package mathandel.backend.model.client;

import mathandel.backend.model.server.enums.EditionStatusName;

import java.util.Set;

public class ModeratorResultsTO {

    private EditionStatusName status;
    private Set<ResultTO> results;

    public EditionStatusName getStatus() {
        return status;
    }

    public ModeratorResultsTO setStatus(EditionStatusName status) {
        this.status = status;
        return this;
    }

    public Set<ResultTO> getResults() {
        return results;
    }

    public ModeratorResultsTO setResults(Set<ResultTO> results) {
        this.results = results;
        return this;
    }
}
