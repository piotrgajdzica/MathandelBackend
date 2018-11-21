package mathandel.backend.model.client;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTOTest {

    private String payload = "{\"id\":1,\"name\":\"Item name\",\"description\":\"Item description\",\"userId\":1}";
    private ItemTO itemTO = new ItemTO()
            .setId(1L)
            .setName("Item name")
            .setDescription("Item description")
            .setUserId(1L);
    private Gson gson = new Gson();

    @Test
    public void shouldMarshalItemTO() {
        //when
        String actual = gson.toJson(itemTO);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalItemTO() {
        //when
        ItemTO actual = gson.fromJson(payload, ItemTO.class);

        //then
        assertThat(actual).isEqualTo(itemTO);
    }
}
