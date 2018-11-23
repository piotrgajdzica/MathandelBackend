package mathandel.backend.model.client;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

public class RateTO {

    private Long resultId;
    @NotBlank
    @Range(min = 0, max = 5)
    private int rate;
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

    public @NotBlank int getRate() {
        return rate;
    }

    public RateTO setRate(int rate) {
        this.rate = rate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RateTO rateTO = (RateTO) o;

        return new EqualsBuilder()
                .append(resultId, rateTO.resultId)
                .append(rate, rateTO.rate)
                .append(comment, rateTO.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(resultId)
                .append(rate)
                .append(comment)
                .toHashCode();
    }
}
