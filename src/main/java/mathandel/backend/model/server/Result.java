package mathandel.backend.model.server;


import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @OneToOne
    private Product productToSend;

    @ManyToOne
    private Edition edition;

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

    public Product getProductToSend() {
        return productToSend;
    }

    public Result setProductToSend(Product productToSend) {
        this.productToSend = productToSend;
        return this;
    }


}
