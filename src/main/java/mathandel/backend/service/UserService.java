package mathandel.backend.service;

import mathandel.backend.model.User;
import mathandel.backend.payload.request.SignUpRequest;
import mathandel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ServiceResponse createUser(SignUpRequest signUpRequest) {

        ServiceResponse serviceResponse = new ServiceResponse();

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            serviceResponse.setMessage("Email already in use");
            serviceResponse.setSuccess(false);
            return serviceResponse;
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setSurname(signUpRequest.getSurname());
        user.setPassword(signUpRequest.getPassword());

        userRepository.save(user);

        serviceResponse.setSuccess(true);
        serviceResponse.setMessage("User created successfully");
        return serviceResponse;
    }
}
