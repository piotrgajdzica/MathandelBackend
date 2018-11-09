package mathandel.backend.model.client;

import com.google.gson.Gson;
import mathandel.backend.model.client.RoleTO;
import mathandel.backend.model.server.enums.RoleName;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTOTest {

    private String payload = "{\"roleName\":\"ROLE_USER\"}";
    private RoleTO roleTO = new RoleTO()
            .setRoleName(RoleName.ROLE_USER);
    private Gson gson = new Gson();

    @Test
    public void shouldMarshalRoleTO() {
        //when
        String actual = gson.toJson(roleTO);

        //then
        assertThat(actual).isEqualTo(payload);
    }

    @Test
    public void shouldUnmarshalRoleTO() {
        //when
        RoleTO actual = gson.fromJson(payload, RoleTO.class);

        //then
        assertThat(actual).isEqualTo(roleTO);
    }
}