package mathandel.backend.model.server;

import mathandel.backend.model.server.enums.RateName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RateName name;

    public Rate() {
    }

    public Long getId() {
        return id;
    }

    public Rate setId(Long id) {
        this.id = id;
        return this;
    }

    public RateName getName() {
        return name;
    }

    public Rate setName(RateName name) {
        this.name = name;
        return this;
    }
}
