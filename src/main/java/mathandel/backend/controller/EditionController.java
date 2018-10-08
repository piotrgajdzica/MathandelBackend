package mathandel.backend.controller;

import mathandel.backend.client.request.EditionDataRequest;
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

import static mathandel.backend.utils.ResponseCreator.createResponse;

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
    public ResponseEntity<?> createEdition(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody EditionDataRequest editionDataRequest) {
        return createResponse(editionService.createEdition(editionDataRequest, currentUser.getId()));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getEditions() {
        return ResponseEntity.ok(editionService.getEditions());
    }

    @PutMapping("{editionId}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> editEdition(@CurrentUser UserPrincipal currentUser,
                                         @Valid @RequestBody EditionDataRequest editionDataRequest,
                                         @PathVariable("editionId") Long editionId) {
        return createResponse(editionService.editEdition(editionDataRequest, editionId, currentUser.getId()));
    }

    @PostMapping("{editionId}/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinEdition(@CurrentUser UserPrincipal currentUser, @PathVariable("editionId") Long editionId) {
        return createResponse(userService.joinEdition(currentUser.getId(), editionId));
    }

    @PutMapping("{editionId}/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> assignProductToEdition(@CurrentUser UserPrincipal currentUser, @PathVariable("editionId") Long editionId, @PathVariable Long productId) {
        return createResponse(productService.assignProductToEdition(currentUser.getId(), editionId, productId));
    }

    @GetMapping("{editionId}/products")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductsFromEdition(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return ResponseEntity.ok(productService.getProductsFromEdition(currentUser.getId(), editionId));
    }

    @GetMapping("{editionId}/products/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyProductsFromEdition(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return ResponseEntity.ok(productService.getMyProductsFromEdition(currentUser.getId(), editionId));
    }

}
