package mathandel.backend.model.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInRequestTest {

    private String payload = "{\"usernameOrEmail\":\"email@email.com\",\"password\":\"Password\"}";
    private SignInRequest signInRequest = new SignInRequest()
            .setPassword("Password")
            .setUsernameOrEmail("email@email.com");
    private Gson gson = new Gson();

    @Test
    public void shouldMarshalLoginReques() {
        //when
        String actual = gson.toJson(signInRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalLoginReques() {
        //when
        SignInRequest actual = gson.fromJson(payload, SignInRequest.class);

        //then
        assertThat(actual).isEqualTo(signInRequest);
    }
}