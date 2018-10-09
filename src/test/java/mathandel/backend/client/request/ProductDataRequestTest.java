package mathandel.backend.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductDataRequestTest {

    private String payload = "{\"name\":\"Name\",\"description\":\"Description\"}";
    private ProductDataRequest productDataRequest = new ProductDataRequest()
            .setName("Name")
            .setDescription("Description");
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalProductDataRequest() {
        //when
        String actual = gson.toJson(productDataRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalProductDataRequest() {
        //when
        ProductDataRequest actual = gson.fromJson(payload, ProductDataRequest.class);

        //then
        assertThat(actual).isEqualTo(productDataRequest);
    }
}