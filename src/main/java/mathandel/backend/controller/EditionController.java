package mathandel.backend.controller;

import mathandel.backend.payload.request.AddEditEditionRequest;
import mathandel.backend.payload.request.AssignProductRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.EditionService;
import mathandel.backend.service.ProductService;
import mathandel.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/editions")
public class EditionController {

    private final EditionService editionService;
    private final UserService userService;
    private final ProductService productService;

    public EditionController(EditionService editionService, UserService userService, ProductService productService) {
        this.editionService = editionService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addEdition(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody AddEditEditionRequest addEditEditionRequest) {
        ApiResponse apiResponse = editionService.createEdition(addEditEditionRequest, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getEditions() {
        return ResponseEntity.ok(editionService.getEditions());
    }

    @PutMapping("{editionId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> editEdition(@CurrentUser UserPrincipal currentUser,
                                         @Valid @RequestBody AddEditEditionRequest addEditEditionRequest,
                                         @PathVariable("editionId") Long editionId) {
        ApiResponse apiResponse = editionService.editEdition(addEditEditionRequest, editionId, currentUser.getId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @PostMapping("{editionId}/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinEdition(@CurrentUser UserPrincipal currentUser, @PathVariable("editionId") Long editionId) {
        ApiResponse apiResponse = userService.joinEdition(currentUser.getId(), editionId);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @PostMapping("{editionId}/products")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> assignProductToEdition(@CurrentUser UserPrincipal currentUser, @PathVariable("editionId") Long editionId, @RequestBody AssignProductRequest assignProductRequest) {
        ApiResponse apiResponse = productService.assignProductToEdition(currentUser.getId(), editionId, assignProductRequest.getProductId());
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping("{editionId}/products")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOthersProducts(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return ResponseEntity.ok(productService.getOthersProductsFromEdition(currentUser.getId(), editionId));
    }

    @GetMapping("{editionId}/products/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyProducts(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return ResponseEntity.ok(productService.getMyProductsFromEdition(currentUser.getId(), editionId));
    }

}
