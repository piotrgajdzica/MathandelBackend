package mathandel.backend.model.client;

import java.util.Set;

public class UserResultsTO {

    private Set<ResultTO> resultsToSend;
    private Set<ResultTO> resultsToReceive;
    private Set<SenderTO> senders;
    private Set<ReceiverTO> receivers;

    public Set<ResultTO> getResultsToSend() {
        return resultsToSend;
    }

    public UserResultsTO setResultsToSend(Set<ResultTO> resultsToSend) {
        this.resultsToSend = resultsToSend;
        return this;
    }

    public Set<ResultTO> getResultsToReceive() {
        return resultsToReceive;
    }

    public UserResultsTO setResultsToReceive(Set<ResultTO> resultsToReceive) {
        this.resultsToReceive = resultsToReceive;
        return this;
    }

    public Set<SenderTO> getSenders() {
        return senders;
    }

    public UserResultsTO setSenders(Set<SenderTO> senders) {
        this.senders = senders;
        return this;
    }

    public Set<ReceiverTO> getReceivers() {
        return receivers;
    }

    public UserResultsTO setReceivers(Set<ReceiverTO> receivers) {
        this.receivers = receivers;
        return this;
    }
}
