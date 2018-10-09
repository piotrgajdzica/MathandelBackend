package mathandel.backend.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpRequestTest {

    private String payload = "{\"name\":\"Name\",\"surname\":\"Surname\",\"username\":\"Username\",\"email\":\"email@email.com\",\"password\":\"Password\"}";
    private SignUpRequest signUpRequest = new SignUpRequest()
            .setName("Name")
            .setSurname("Surname")
            .setUsername("Username")
            .setEmail("email@email.com")
            .setPassword("Password");
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalSignUpRequest() {
        //when
        String actual = gson.toJson(signUpRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalSignUpRequest() {
        //when
        SignUpRequest actual = gson.fromJson(payload, SignUpRequest.class);

        //then
        assertThat(actual).isEqualTo(signUpRequest);
    }
}