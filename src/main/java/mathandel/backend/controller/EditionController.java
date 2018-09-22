package mathandel.backend.controller;

import mathandel.backend.payload.request.AddEditEditionRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.UserRepository;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.EditionService;
import mathandel.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/editions")
public class EditionController {

    @Autowired
    EditionRepository editionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EditionService editionService;

    @Autowired
    UserService userService;

    //addEditEditionRequest  nie oop!!!!
    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addEdition(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AddEditEditionRequest addEditEditionRequest){
        ApiResponse apiResponse = editionService.addEdition(addEditEditionRequest, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getEditions(){
        return ResponseEntity.ok(editionService.getEditions());
    }

    @PutMapping("{editionId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> editEdition(@CurrentUser UserPrincipal currentUser,
                                         @Valid @RequestBody AddEditEditionRequest addEditEditionRequest,
                                         @PathVariable("editionId") Long editionId){
        ApiResponse apiResponse = editionService.editEdition(addEditEditionRequest, editionId, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @PostMapping("{editionId}/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinEdition(@CurrentUser UserPrincipal currentUser, @PathVariable("editionId") Long editionId){
        ApiResponse apiResponse = userService.joinEdition(currentUser.getId(), editionId);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

}
