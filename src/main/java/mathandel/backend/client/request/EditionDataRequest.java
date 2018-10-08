package mathandel.backend.client.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EditionDataRequest {

    @NotBlank
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    @NotBlank
    private String description;
    @NotNull
    @Range(min = 1)
    private Integer maxParticipants;

    public String getName() {
        return name;
    }

    public EditionDataRequest setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EditionDataRequest setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }



    public String getDescription() {
        return description;
    }

    public EditionDataRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public EditionDataRequest setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
        return this;
    }
}