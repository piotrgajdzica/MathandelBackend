package mathandel.backend.client.model;

import com.google.gson.Gson;
import mathandel.backend.model.client.ProductTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTOTest {

    private String payload = "{\"id\":1,\"name\":\"Product name\",\"description\":\"Product description\",\"userId\":1}";
    private ProductTO productTO = new ProductTO()
            .setId(1L)
            .setName("Product name")
            .setDescription("Product description")
            .setUserId(1L);
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalProductTO() {
        //when
        String actual = gson.toJson(productTO);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalProductTO() {
        //when
        ProductTO actual = gson.fromJson(payload, ProductTO.class);

        //then
        assertThat(actual).isEqualTo(productTO);
    }
}
