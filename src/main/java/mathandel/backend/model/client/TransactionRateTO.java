package mathandel.backend.model.client;

import mathandel.backend.model.server.enums.RateName;

import java.util.Objects;

public class TransactionRateTO {

    private Long id;
    private Long raterId;
    private Long resultId;
    private RateName rateName;
    private String comment;

    public String getComment() {
        return comment;
    }

    public TransactionRateTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TransactionRateTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRaterId() {
        return raterId;
    }

    public TransactionRateTO setRaterId(Long raterId) {
        this.raterId = raterId;
        return this;
    }

    public Long getResultId() {
        return resultId;
    }

    public TransactionRateTO setResultId(Long resultId) {
        this.resultId = resultId;
        return this;
    }

    public RateName getRateName() {
        return rateName;
    }

    public TransactionRateTO setRateName(RateName rateName) {
        this.rateName = rateName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionRateTO)) return false;
        TransactionRateTO transactionRateTO = (TransactionRateTO) o;
        return Objects.equals(id, transactionRateTO.id) &&
                Objects.equals(raterId, transactionRateTO.raterId) &&
                Objects.equals(resultId, transactionRateTO.resultId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, raterId, resultId);
    }
}
