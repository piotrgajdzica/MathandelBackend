package mathandel.backend.component;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.model.server.enums.ModeratorRequestStatusName;
import mathandel.backend.model.server.enums.RateName;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DBDataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private final RateRepository rateRepository;
    private final EditionRepository editionRepository;
    private final DefinedGroupRepository definedGroupRepository;
    private final ProductRepository productRepository;
    private final ResultRepository resultRepository;
    private final TransactionRateRepository transactionRateRepository;
    private final PreferenceRepository preferenceRepository;
    private  FullDBPopulator fullDBPopulator;
    private final ModeratorRequestStatusRepository moderatorRequestStatusRepository;


    public DBDataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, EditionStatusTypeRepository editionStatusTypeRepository, RateRepository rateRepository, EditionRepository editionRepository, DefinedGroupRepository definedGroupRepository, ProductRepository productRepository, ResultRepository resultRepository, TransactionRateRepository transactionRateRepository, PreferenceRepository preferenceRepository, ModeratorRequestStatusRepository moderatorRequestStatusRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
        this.rateRepository = rateRepository;
        this.editionRepository = editionRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.productRepository = productRepository;
        this.resultRepository = resultRepository;
        this.transactionRateRepository = transactionRateRepository;
        this.preferenceRepository = preferenceRepository;
        this.moderatorRequestStatusRepository = moderatorRequestStatusRepository;
        this.fullDBPopulator = new FullDBPopulator(roleRepository,userRepository,editionStatusTypeRepository,rateRepository,editionRepository,definedGroupRepository,productRepository,resultRepository,transactionRateRepository, this.preferenceRepository);
    }

    @Override
    public void run(ApplicationArguments args) {
        insertRolesToDB();
        insertEditionStatusesToDB();
        insertAdminToDB();
        insertRatesToDB();
        insertModeratorRequestStatusesToDB();
        fullDBPopulator.populate();
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

    private void insertRatesToDB() {
        for (RateName rateName : RateName.values()) {
            Rate rate = new Rate();
            rate.setName(rateName);
            rateRepository.save(rate);
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
        for (ModeratorRequestStatusName moderatorRequestStatusName: ModeratorRequestStatusName.values()) {
            moderatorRequestStatusRepository.save(new ModeratorRequestStatus().setName(moderatorRequestStatusName));
        }
    }
}

