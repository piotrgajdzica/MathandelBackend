package mathandel.backend.payload.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class AddEditEditionRequest {

    @NotBlank
    private String name;

    @JsonDeserialize(using= com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer.class)
    private LocalDate endDate;

    private Integer maxUsers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
