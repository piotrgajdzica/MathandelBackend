package mathandel.backend.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "moderator_requests_status")
public class ModeratorRequestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private ModeratorRequestStatusName name;

    public ModeratorRequestStatus() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModeratorRequestStatusName getName() {
        return name;
    }

    public void setName(ModeratorRequestStatusName name) {
        this.name = name;
    }
}
