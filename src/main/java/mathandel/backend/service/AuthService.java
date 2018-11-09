package mathandel.backend.service;

import mathandel.backend.model.client.request.SignInFacebookRequest;
import mathandel.backend.model.client.request.SignInRequest;
import mathandel.backend.model.client.request.SignUpFacebookRequest;
import mathandel.backend.model.client.request.SignUpRequest;
import mathandel.backend.model.client.response.FacebookJwtAuthenticationResponse;
import mathandel.backend.model.client.response.FacebookResponse;
import mathandel.backend.model.client.response.JwtAuthenticationResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.server.Role;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.RoleRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapRoles;

@Service
public class AuthService {

    @Value("${facebook.secret}")
    private String facebookPassword;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse()
                .setAccessToken(getToken(signInRequest.getUsernameOrEmail(), signInRequest.getPassword()));

        User user = userRepository.findByUsernameOrEmail(signInRequest.getUsernameOrEmail(), signInRequest.getUsernameOrEmail())
                .orElseThrow(() -> new AppException("User does not exist"));

        return response.setRoles(mapRoles(user.getRoles()));
    }

    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(
                () -> new AppException("User Role not in database"));

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already in use");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use");
        }

        Set<Role> roles = Collections.singleton(userRole);

        User user = new User()
                .setName(signUpRequest.getName())
                .setSurname(signUpRequest.getSurname())
                .setUsername(signUpRequest.getUsername())
                .setEmail(signUpRequest.getEmail())
                .setPassword(passwordEncoder.encode(signUpRequest.getPassword()))
                .setAddress(signUpRequest.getAddress())
                .setCity(signUpRequest.getCity())
                .setPostalCode(signUpRequest.getPostalCode())
                .setCountry(signUpRequest.getCountry())
                .setRoles(roles);

        userRepository.save(user);
        return new JwtAuthenticationResponse()
                .setAccessToken(getToken(signUpRequest.getEmail(), signUpRequest.getPassword()))
                .setRoles(mapRoles(user.getRoles()));
    }

    public FacebookResponse facebookSignIn(SignInFacebookRequest signInFacebookRequest) {

        Facebook facebook = new FacebookTemplate(signInFacebookRequest.getToken());
        String[] fields = {"id"};
        org.springframework.social.facebook.api.User userProfile =
                facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);

        if (!userRepository.existsByFacebookId(userProfile.getId())) {
            return new FacebookResponse().setUserExists(false);
        }

        mathandel.backend.model.server.User user = userRepository.findByFacebookId(userProfile.getId())
                .orElseThrow(() -> new AppException("User does not exist"));

        return new FacebookJwtAuthenticationResponse()
                .setAccessToken(getToken(user.getEmail(), facebookPassword))
                .setRoles(mapRoles(user.getRoles()))
                .setUserExists(true);
    }

    public FacebookResponse facebookSignUp(SignUpFacebookRequest signUpFacebookRequest) {

        Facebook facebook = new FacebookTemplate(signUpFacebookRequest.getToken());
        String[] fields = {"id"};
        org.springframework.social.facebook.api.User userProfile =
                facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);

        if (userRepository.existsByFacebookId(userProfile.getId())) {
            throw new BadRequestException("User already exists");
        }
        if (userRepository.existsByUsername(signUpFacebookRequest.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (userRepository.existsByEmail(signUpFacebookRequest.getEmail())) {
            throw new BadRequestException("Email already taken");
        }

        mathandel.backend.model.server.User user = new mathandel.backend.model.server.User()
                .setName(signUpFacebookRequest.getName())
                .setSurname(signUpFacebookRequest.getSurname())
                .setUsername(signUpFacebookRequest.getUsername())
                .setEmail(signUpFacebookRequest.getEmail())
                .setFacebookId(userProfile.getId())
                .setAddress(signUpFacebookRequest.getAddress())
                .setCity(signUpFacebookRequest.getCity())
                .setPostalCode(signUpFacebookRequest.getPostalCode())
                .setCountry(signUpFacebookRequest.getCountry())
                .setRoles(Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(
                        () -> new AppException("User Role not in database"))))
                .setPassword(passwordEncoder.encode(facebookPassword));

        userRepository.save(user);

        return new FacebookJwtAuthenticationResponse()
                .setAccessToken(getToken(user.getEmail(), facebookPassword))
                .setRoles(mapRoles(user.getRoles()))
                .setUserExists(true);
    }

    private String getToken(String usernameOrEmail, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }
}
