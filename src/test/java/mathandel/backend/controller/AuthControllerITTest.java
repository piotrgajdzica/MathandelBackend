package mathandel.backend.controller;

import com.google.gson.Gson;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private Gson gson = new Gson();

    @Test
    @Transactional
    public void shouldSignUpAndCreateUserInDB() throws Exception {
        //given
        String name = "John";
        String surname = "Smith";
        String username = "jsmith";
        String email = "jsmith@gmail.com";
        String password = "jsmith123";

        SignUpRequest signUpRequest = new SignUpRequest()
                .setName(name)
                .setSurname(surname)
                .setUsername(username)
                .setEmail(email)
                .setPassword(password);

        //when
        mockMvc.perform(post("/api/auth/signup")
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"User registered successfully\"}"));

        //then
        userRepository.findByUsername(username).ifPresent(user -> {
            assertThat(user.getName()).isEqualTo(name);
            assertThat(user.getSurname()).isEqualTo(surname);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getUsername()).isEqualTo(username);
        });
    }
}