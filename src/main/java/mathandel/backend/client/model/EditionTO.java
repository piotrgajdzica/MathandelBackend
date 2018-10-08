package mathandel.backend.client.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class EditionTO {

    private Long Id;
    private String name;
    private String description;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;
    private int maxParticipants;
    private int numberOfParticipants;

    public Long getId() {
        return Id;
    }

    public EditionTO setId(Long id) {
        Id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EditionTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EditionTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EditionTO setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public EditionTO setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
        return this;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public EditionTO setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
        return this;
    }
}
