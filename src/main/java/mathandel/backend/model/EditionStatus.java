package mathandel.backend.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "edition_status")
public class EditionStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private EditionStatusName editionStatusName;

    public EditionStatus() {
    }

    public EditionStatus(EditionStatusName editionStatusName) {
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
