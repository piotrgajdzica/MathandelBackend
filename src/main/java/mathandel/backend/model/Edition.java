package mathandel.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import mathandel.backend.serializer.ModeratorsSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "editions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "moderators",
            joinColumns = @JoinColumn(name = "edition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonSerialize(using = ModeratorsSerializer.class)
    private Set<User> moderators = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private EditionStatus editionStatus;

    public Edition() {
    }

    public Edition(String name, LocalDate endDate, Set<User> moderators, EditionStatus editionStatus) {
        this.name = name;
        this.endDate = endDate;
        this.moderators = moderators;
        this.editionStatus = editionStatus;
    }

    public Long getId() {
        return id;
    }

    public Edition setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Edition setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Edition setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Set<User> getModerators() {
        return moderators;
    }

    public Edition setModerators(Set<User> moderators) {
        this.moderators = moderators;
        return this;
    }

    public EditionStatus getEditionStatus() {
        return editionStatus;
    }

    public Edition setEditionStatus(EditionStatus editionStatus) {
        this.editionStatus = editionStatus;
        return this;
    }
}
