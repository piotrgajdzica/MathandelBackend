package mathandel.backend.service;

import mathandel.backend.model.User;
import mathandel.backend.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RoleServiceTest {

    private Long userId = 1L;
    private User user = new User("James", "Smith", "jsmith", "jsmith@gmail.com", "jsmith123");

    @MockBean
    UserRepository userRepository;

    @MockBean
    ModeratorRequestsRepository moderatorRequestsRepository;

    @MockBean
    RoleRepository roleRepository;


    @Test
    public void shouldRequestModerator(){
//        // given
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(moderatorRequestsRepository.findByUser(user)).thenReturn(Optional.of())
//        // when
//
//        // then

    }
}