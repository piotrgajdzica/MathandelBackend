package mathandel.backend.model.server;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "transaction_rates")
public class TransactionRate {

    // todo pododawac size w TO
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User rater;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rate rate;

    @OneToOne
    private Result result;

    @Size(min = 4, max = 160)
    private String comment;

    public Long getId() {
        return id;
    }

    public TransactionRate setId(Long id) {
        this.id = id;
        return this;
    }

    public User getRater() {
        return rater;
    }

    public TransactionRate setRater(User rater) {
        this.rater = rater;
        return this;
    }

    public Rate getRate() {
        return rate;
    }

    public TransactionRate setRate(Rate rate) {
        this.rate = rate;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public TransactionRate setComment(String comment) {
        this.comment = comment;
        return this;
    }


    public Result getResult() {
        return result;
    }

    public TransactionRate setResult(Result result) {
        this.result = result;
        return this;
    }
}
