package mathandel.backend.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDataRequestTest {

    private String payload = "{\"name\":\"Name\",\"surname\":\"Surname\",\"username\":\"Username\",\"email\":\"email@email.com\"}";
    private UserDataRequest userDataRequest = new UserDataRequest()
            .setName("Name")
            .setSurname("Surname")
            .setUsername("Username")
            .setEmail("email@email.com");
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalUserDataRequest() {
        //when
        String actual = gson.toJson(userDataRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalUserDataRequest() {
        //when
        UserDataRequest actual = gson.fromJson(payload, UserDataRequest.class);

        //then
        assertThat(actual).isEqualTo(userDataRequest);
    }
}