package mathandel.backend.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class EditionDataRequestTest {

    private String payload = "{\"name\":\"Name\",\"endDate\":{\"year\":2018,\"month\":10,\"day\":10},\"description\":\"Description\",\"maxParticipants\":100}";
    private EditionDataRequest editionDataRequest = new EditionDataRequest()
            .setName("Name")
            .setDescription("Description")
            .setEndDate(LocalDate.of(2018, 10, 10))
            .setMaxParticipants(100);
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalEditionDataRequest() {
        //when
        String actual = gson.toJson(editionDataRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalEditionDataRequest() {
        //when
        EditionDataRequest actual = gson.fromJson(payload, EditionDataRequest.class);

        //then
        assertThat(actual).isEqualTo(editionDataRequest);
    }
}