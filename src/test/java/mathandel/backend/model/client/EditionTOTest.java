package mathandel.backend.model.client;

import com.google.gson.Gson;
import mathandel.backend.model.client.EditionTO;
import org.junit.Test;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

public class EditionTOTest {

    private String payload = "{\"Id\":1,\"name\":\"Edition name\",\"description\":\"Edition description\",\"endDate\":{\"year\":2018,\"month\":10,\"day\":10},\"maxParticipants\":100,\"numberOfParticipants\":0,\"isModerator\":false,\"isParticipant\":false}";
    private EditionTO editionTO = new EditionTO()
            .setId(1L)
            .setName("Edition name")
            .setDescription("Edition description")
            .setEndDate(LocalDate.of(2018, 10, 10))
            .setMaxParticipants(100);
    private Gson gson = new Gson();

    @Test
    public void shouldMarshalEditionTO() {
        //when
        String actual = gson.toJson(editionTO);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalEditionTO() {
        //when
        EditionTO actual = gson.fromJson(payload, EditionTO.class);

        //then
        assertThat(actual).isEqualTo(editionTO);
    }
}