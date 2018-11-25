package mathandel.backend.component;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.ModeratorRequestStatus;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class SystemInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private final RateTypeRepository rateTypeRepository;
    private final FullDBPopulator fullDBPopulator;
    private final ModeratorRequestStatusRepository moderatorRequestStatusRepository;
    private final TestPopulator testPopulator;
    private final MathandelDataPopulator mathandelDataPopulator;


    public SystemInitializer(RoleRepository roleRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             EditionStatusTypeRepository editionStatusTypeRepository,
                             RateTypeRepository rateTypeRepository,
                             ModeratorRequestStatusRepository moderatorRequestStatusRepository,
                             FullDBPopulator fullDBPopulator, TestPopulator testPopulator,
                             MathandelDataPopulator mathandelDataPopulator) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
        this.rateTypeRepository = rateTypeRepository;
        this.moderatorRequestStatusRepository = moderatorRequestStatusRepository;
        this.fullDBPopulator = fullDBPopulator;
        this.testPopulator = testPopulator;
        this.mathandelDataPopulator = mathandelDataPopulator;
    }

    @Override
    public void run(ApplicationArguments args) {
        insertRolesToDB();
        insertEditionStatusesToDB();
        insertAdminToDB();
        insertModeratorRequestStatusesToDB();

        try {
            mathandelDataPopulator.saveFromFile("/home/monikeu/Dokumenty/mathandel-back/MathandelBackend/src/main/resources/mathandel_example_preference_data/mathandel_30.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        testPopulator.populate(); // -> if you want to use this go to TestPopulator class
//        fullDBPopulator.populate();
    }

    private void insertRolesToDB() {
        for (RoleName roleName : RoleName.values()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    private void insertEditionStatusesToDB() {
        for (EditionStatusName editionStatusName : EditionStatusName.values()) {
            EditionStatusType editionStatusType = new EditionStatusType();
            editionStatusType.setEditionStatusName(editionStatusName);
            editionStatusTypeRepository.save(editionStatusType);
        }
    }

    private void insertAdminToDB() {
        User user = new User()
                .setName("admin")
                .setSurname("admin")
                .setUsername("admin")
                .setEmail("admin@admin.admin")
                .setPassword(passwordEncoder.encode("admin"))
                .setAddress("admin")
                .setCity("admin")
                .setPostalCode("admin")
                .setCountry("admin");

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new AppException("Admin Role not set."));
        Role moderatorRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                .orElseThrow(() -> new AppException("Moderator Role not set."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        roles.add(moderatorRole);
        user.setRoles(roles);

        userRepository.save(user);
    }

    private void insertModeratorRequestStatusesToDB() {
        for (ModeratorRequestStatusName moderatorRequestStatusName : ModeratorRequestStatusName.values()) {
            moderatorRequestStatusRepository.save(new ModeratorRequestStatus().setName(moderatorRequestStatusName));
        }
    }
}

