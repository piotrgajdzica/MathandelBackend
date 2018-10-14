package mathandel.backend.model.server;

import mathandel.backend.model.server.enums.EditionStatusName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "edition_status_types")
public class EditionStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private EditionStatusName editionStatusName;

    public EditionStatusType() {
    }

    public EditionStatusType(EditionStatusName editionStatusName) {
        this.editionStatusName = editionStatusName;
    }

    public Long getId() {
        return id;
    }

    public EditionStatusType setId(Long id) {
        this.id = id;
        return this;
    }

    public EditionStatusName getEditionStatusName() {
        return editionStatusName;
    }

    public EditionStatusType setEditionStatusName(EditionStatusName editionStatusName) {
        this.editionStatusName = editionStatusName;
        return this;
    }
}
