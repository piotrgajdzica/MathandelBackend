package mathandel.backend.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginRequestTest {

    private String payload = "{\"usernameOrEmail\":\"email@email.com\",\"password\":\"Password\"}";
    private LoginRequest loginRequest = new LoginRequest()
            .setPassword("Password")
            .setUsernameOrEmail("email@email.com");
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalLoginReques() {
        //when
        String actual = gson.toJson(loginRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalLoginReques() {
        //when
        LoginRequest actual = gson.fromJson(payload, LoginRequest.class);

        //then
        assertThat(actual).isEqualTo(loginRequest);
    }
}