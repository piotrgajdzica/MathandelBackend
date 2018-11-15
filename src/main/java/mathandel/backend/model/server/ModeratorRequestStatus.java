package mathandel.backend.model.server;

import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
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

    public ModeratorRequestStatus() {
    }

    public Long getId() {
        return id;
    }

    public ModeratorRequestStatus setId(Long id) {
        this.id = id;
        return this;
    }

    public ModeratorRequestStatusName getName() {
        return name;
    }

    public ModeratorRequestStatus setName(ModeratorRequestStatusName name) {
        this.name = name;
        return this;
    }
}
