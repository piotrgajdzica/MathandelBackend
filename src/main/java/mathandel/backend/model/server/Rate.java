package mathandel.backend.model.server;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User rater;

    @Range(min = 0, max = 5)
    private int rate;

    @OneToOne
    private Result result;

    private String comment;

    public Long getId() {
        return id;
    }

    public Rate setId(Long id) {
        this.id = id;
        return this;
    }

    public User getRater() {
        return rater;
    }

    public Rate setRater(User rater) {
        this.rater = rater;
        return this;
    }

    public int getRate() {
        return rate;
    }

    public Rate setRate(int rate) {
        this.rate = rate;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Rate setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public Rate setResult(Result result) {
        this.result = result;
        return this;
    }
}
