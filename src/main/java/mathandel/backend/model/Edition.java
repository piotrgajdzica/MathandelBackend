package mathandel.backend.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import mathandel.backend.serializer.UserSerializer;

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
    private String description;
    private LocalDate endDate;
    private int maxParticipants;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "moderators",
            joinColumns = @JoinColumn(name = "edition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> moderators = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "participants",
            joinColumns = @JoinColumn(name = "edition_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private EditionStatusType editionStatusType;

    public Edition() {
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

    public String getDescription() {
        return description;
    }

    public Edition setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Edition setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Edition setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
        return this;
    }

    public Set<User> getModerators() {
        return moderators;
    }

    public Edition setModerators(Set<User> moderators) {
        this.moderators = moderators;
        return this;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public Edition setParticipants(Set<User> participants) {
        this.participants = participants;
        return this;
    }

    public EditionStatusType getEditionStatusType() {
        return editionStatusType;
    }

    public Edition setEditionStatusType(EditionStatusType editionStatusType) {
        this.editionStatusType = editionStatusType;
        return this;
    }
}
