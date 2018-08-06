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
import mathandel.backend.service.EditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/editions")
public class EditionController {

    @Autowired
    EditionRepository editionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EditionService editionService;

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addEdition(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AddEditionRequest addEditionRequest){

        ApiResponse apiResponse = editionService.addEdition(addEditionRequest, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getEditions(){

        return ResponseEntity.ok(editionService.getEditions());
    }

}
