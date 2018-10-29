package mathandel.backend.model.client.request;

import com.google.gson.Gson;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordRequestTest {

    private String payload = "{\"newPassword\":\"New Password\"}";
    private PasswordRequest passwordRequest = new PasswordRequest()
            .setNewPassword("New Password");
    private Gson gson = new Gson();


    @Test
    public void shouldMarshalPasswordRequest() {
        //when
        String actual = gson.toJson(passwordRequest);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalPasswordRequest() {
        //when
        PasswordRequest actual = gson.fromJson(payload, PasswordRequest.class);

        //then
        assertThat(actual).isEqualTo(passwordRequest);
    }
}