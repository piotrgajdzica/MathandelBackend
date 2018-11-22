package mathandel.backend.model.client;

import java.util.Set;

public class ResultsTO {

    private Set<ResultTO> resultsToSend;
    private Set<ResultTO> resultsToReceive;
    private Set<SenderTO> senders;
    private Set<ReceiverTO> receivers;

    public Set<ResultTO> getResultsToSend() {
        return resultsToSend;
    }

    public ResultsTO setResultsToSend(Set<ResultTO> resultsToSend) {
        this.resultsToSend = resultsToSend;
        return this;
    }

    public Set<ResultTO> getResultsToReceive() {
        return resultsToReceive;
    }

    public ResultsTO setResultsToReceive(Set<ResultTO> resultsToReceive) {
        this.resultsToReceive = resultsToReceive;
        return this;
    }

    public Set<SenderTO> getSenders() {
        return senders;
    }

    public ResultsTO setSenders(Set<SenderTO> senders) {
        this.senders = senders;
        return this;
    }

    public Set<ReceiverTO> getReceivers() {
        return receivers;
    }

    public ResultsTO setReceivers(Set<ReceiverTO> receivers) {
        this.receivers = receivers;
        return this;
    }
}
