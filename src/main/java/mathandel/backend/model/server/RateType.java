package mathandel.backend.model.server;

import mathandel.backend.model.server.enums.RateTypeName;

import javax.persistence.*;

@Entity
@Table(name = "rate_types")
public class RateType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RateTypeName name;

    public RateType() {
    }

    public Long getId() {
        return id;
    }

    public RateType setId(Long id) {
        this.id = id;
        return this;
    }

    public RateTypeName getName() {
        return name;
    }

    public RateType setName(RateTypeName name) {
        this.name = name;
        return this;
    }
}
