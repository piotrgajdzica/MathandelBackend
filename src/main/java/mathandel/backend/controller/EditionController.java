package mathandel.backend.controller;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.User;
import mathandel.backend.payload.request.AddEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/api/edition")
public class EditionController {

    @Autowired
    EditionRepository editionRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> add(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AddEditionRequest addEditionRequest){

        if (editionRepository.existsByName(addEditionRequest.getName())) {
            return new ResponseEntity<>(new ApiResponse(false, "Edition name already exists!"),
                    HttpStatus.BAD_REQUEST);
        }

        Set<User> moderators = new HashSet<>();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("User doesn't exist."));
        moderators.add(user);

        Edition edition = new Edition();
        edition.setName(addEditionRequest.getName());
        edition.setEndDate(addEditionRequest.getEndDate());
        edition.setModerators(moderators);
        user.getEditions().add(edition);

        editionRepository.save(edition);
        userRepository.save(user);



        return ResponseEntity.ok(new ApiResponse(true, "Edition added successfully"));
    }
}
