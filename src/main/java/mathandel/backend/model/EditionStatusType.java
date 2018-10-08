package mathandel.backend.model;

import mathandel.backend.model.enums.EditionStatusName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "edition_status_type")
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

    public void setId(Long id) {
        this.id = id;
    }

    public EditionStatusName getEditionStatusName() {
        return editionStatusName;
    }

    public void setEditionStatusName(EditionStatusName editionStatusName) {
        this.editionStatusName = editionStatusName;
    }
}
