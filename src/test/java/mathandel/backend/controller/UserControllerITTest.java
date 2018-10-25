package mathandel.backend.controller;

import com.google.gson.Gson;
import mathandel.backend.model.client.RoleTO;
import mathandel.backend.model.client.UserTO;
import mathandel.backend.client.request.SignInRequest;
import mathandel.backend.client.request.SignUpRequest;
import mathandel.backend.client.response.JwtAuthenticationResponse;
import mathandel.backend.model.server.enums.RoleName;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerITTest {

    @Autowired
    MockMvc mockMvc;

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

    private SignInRequest signInRequest = new SignInRequest()
            .setUsernameOrEmail(email)
            .setPassword(password);

    private Gson gson = new Gson();

    @Test
    @Transactional
    public void shouldGetMyData() throws Exception {
        //given
        String token = acquireTokenNormalUser();

        //when
        MvcResult result = mockMvc.perform(get("/api/users/me")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authorization(token)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        UserTO userTO = gson.fromJson(result.getResponse().getContentAsString(), UserTO.class);
        assertThat(userTO.getName()).isEqualTo(name);
        assertThat(userTO.getSurname()).isEqualTo(surname);
        assertThat(userTO.getUsername()).isEqualTo(username);
        assertThat(userTO.getEmail()).isEqualTo(email);
        assertThat(userTO.getRoles()).isEqualTo(Collections.singleton(new RoleTO().setRoleName(RoleName.ROLE_USER)));
    }

    @Test
    @Transactional
    public void shouldGetAdminData() throws Exception {
        //given
        Set<RoleTO> roles = new HashSet<>();
        roles.add(new RoleTO().setRoleName(RoleName.ROLE_USER));
        roles.add(new RoleTO().setRoleName(RoleName.ROLE_ADMIN));
        roles.add(new RoleTO().setRoleName(RoleName.ROLE_MODERATOR));
        UserTO admin = new UserTO()
                .setName("admin")
                .setSurname("admin")
                .setUsername("admin")
                .setEmail("admin@admin.admin")
                .setId(1L)
                .setRoles(roles);
        String token = acquireTokenNormalUser();

        //when
        MvcResult mvcResult =  mockMvc.perform(get("/api/users/1")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authorization(token)))
                .andExpect(status().isOk())
                .andReturn();
        UserTO actual = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserTO.class);

        //then
        assertThat(actual).isEqualTo(admin);

    }

    private String acquireTokenNormalUser() {
        authController.signUp(signUpRequest);
        JwtAuthenticationResponse jwtAuthenticationResponse = authController.signIn(signInRequest);
        return Objects.requireNonNull(jwtAuthenticationResponse).getAccessToken();
    }

    private String authorization(String token) {
        return "Bearer " + token;
    }
}