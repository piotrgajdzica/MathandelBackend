package mathandel.backend.model.client;

import mathandel.backend.model.server.enums.RateTypeName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RateTO {

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

        if (o == null || getClass() != o.getClass()) return false;

        RateTO rateTO = (RateTO) o;

        return new EqualsBuilder()
                .append(resultId, rateTO.resultId)
                .append(rateTypeName, rateTO.rateTypeName)
                .append(comment, rateTO.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(resultId)
                .append(rateTypeName)
                .append(comment)
                .toHashCode();
    }
}
