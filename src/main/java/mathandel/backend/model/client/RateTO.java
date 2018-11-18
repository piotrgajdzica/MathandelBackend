package mathandel.backend.model.client;

import mathandel.backend.model.server.enums.RateTypeName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RateTO {

    private Long id;
    private Long raterId;
    private Long resultId;
    @NotNull
    private RateTypeName rateTypeName;
    @NotBlank
    private String comment;

    public String getComment() {
        return comment;
    }

    public RateTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Long getId() {
        return id;
    }

    public RateTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRaterId() {
        return raterId;
    }

    public RateTO setRaterId(Long raterId) {
        this.raterId = raterId;
        return this;
    }

    public Long getResultId() {
        return resultId;
    }

    public RateTO setResultId(Long resultId) {
        this.resultId = resultId;
        return this;
    }

    public RateTypeName getRateTypeName() {
        return rateTypeName;
    }

    public RateTO setRateTypeName(RateTypeName rateTypeName) {
        this.rateTypeName = rateTypeName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateTO)) return false;
        RateTO rateTO = (RateTO) o;
        return Objects.equals(id, rateTO.id) &&
                Objects.equals(raterId, rateTO.raterId) &&
                Objects.equals(resultId, rateTO.resultId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, raterId, resultId);
    }
}
