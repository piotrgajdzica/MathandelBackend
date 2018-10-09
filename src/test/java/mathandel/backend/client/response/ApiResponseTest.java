package mathandel.backend.client.response;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiResponseTest {

    private String payload = "{\"success\":true,\"message\":\"User created successfully\"}";
    private ApiResponse apiResponse = new ApiResponse(true, "User created successfully");
    private Gson gson = new Gson();

    @Test
    public void shouldMarshalApiResponse() {
        //when
        String actual = gson.toJson(apiResponse);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalApiResponse() {
        //when
        ApiResponse actual = gson.fromJson(payload, ApiResponse.class);

        //then
        assertThat(actual).isEqualTo(apiResponse);
    }
}