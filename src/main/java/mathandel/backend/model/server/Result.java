package mathandel.backend.model.server;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @OneToOne
    private Item itemToSend;

    @ManyToOne
    private Edition edition;

    @OneToOne
    private Rate rate;

    public Edition getEdition() {
        return edition;
    }

    public Result setEdition(Edition edition) {
        this.edition = edition;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Result setId(Long id) {
        this.id = id;
        return this;
    }

    public User getSender() {
        return sender;
    }

    public Result setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public User getReceiver() {
        return receiver;
    }

    public Result setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public Item getItemToSend() {
        return itemToSend;
    }

    public Result setItemToSend(Item itemToSend) {
        this.itemToSend = itemToSend;
        return this;
    }

    public Rate getRate() {
        return rate;
    }

    public Result setRate(Rate rate) {
        this.rate = rate;
        return this;
    }
}
