package mathandel.backend.controller;

import com.google.gson.Gson;
import mathandel.backend.model.client.request.SignInRequest;
import mathandel.backend.model.client.request.SignUpRequest;
import mathandel.backend.model.client.response.JwtAuthenticationResponse;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static mathandel.backend.utils.UrlPaths.signInPath;
import static mathandel.backend.utils.UrlPaths.signUpPath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    AuthController authController;

    private final String name = "John";
    private final String surname = "Smith";
    private final String username = "jsmith";
    private final String email = "jsmith@gmail.com";
    private final String password = "jsmith123";
    private final String address = "address";
    private final String city = "city";
    private final String postaCode = "postal code";
    private final String coutry = "country";

    private Gson gson = new Gson();

    private SignUpRequest signUpRequest = new SignUpRequest()
            .setName(name)
            .setSurname(surname)
            .setUsername(username)
            .setEmail(email)
            .setPassword(password)
            .setAddress(address)
            .setCity(city)
            .setPostalCode(postaCode)
            .setCountry(coutry);

    @Test
    @Transactional
    public void shouldSignUpAndCreateUserInDB() throws Exception {
        //when
        mockMvc.perform(post(signUpPath)
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signUpRequest)))
                .andExpect(status().isOk());

        //then
        userRepository.findByUsername(username).ifPresent(user -> {
            assertThat(user.getName()).isEqualTo(name);
            assertThat(user.getSurname()).isEqualTo(surname);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getUsername()).isEqualTo(username);
        });
    }

    @Test
    @Transactional
    public void shouldNotSignUpWhenSameUsername() throws Exception {
        //when
        mockMvc.perform(post(signUpPath)
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signUpRequest)));

        //then
        MvcResult mvcResult = mockMvc.perform(post(signUpPath)
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signUpRequest.setEmail("diffrent@email.com"))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Transactional
    public void shouldSignIn() throws Exception {
        //given
        SignInRequest signInRequest = new SignInRequest()
                .setUsernameOrEmail(email)
                .setPassword(password);
        authController.signUp(signUpRequest);

        //when
        MvcResult mvcResult = mockMvc.perform(post(signInPath)
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signInRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JwtAuthenticationResponse jwtAuthenticationResponse = gson.fromJson(mvcResult.getResponse().getContentAsString(), JwtAuthenticationResponse.class);
        assertThat(jwtAuthenticationResponse.getAccessToken().length()).isEqualTo(168);
    }

    @Test
    @Transactional
    public void shouldNotSignInOnBadCredentials() throws Exception {
        //given
        SignInRequest signInRequest = new SignInRequest()
                .setUsernameOrEmail(email)
                .setPassword(password);
        authController.signUp(signUpRequest);

        //when
        mockMvc.perform(post(signInPath)
                .contentType(APPLICATION_JSON_UTF8)
                .content(gson.toJson(signInRequest.setPassword("wrong password"))))
                .andExpect(status().isUnauthorized())
                .andExpect(status().reason("Sorry, You're not authorized to access this resource."));
    }
}