package mathandel.backend.component;

import mathandel.backend.model.Role;
import mathandel.backend.model.RoleName;
import mathandel.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderComponent implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(RoleName roleName: RoleName.values()){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}

