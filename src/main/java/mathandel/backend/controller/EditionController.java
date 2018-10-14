package mathandel.backend.controller;

import mathandel.backend.client.model.EditionTO;
import mathandel.backend.client.model.PreferenceTO;
import mathandel.backend.client.model.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.EditionService;
import mathandel.backend.service.PreferenceService;
import mathandel.backend.service.ProductService;
import mathandel.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
@RequestMapping("/api/editions")
public class EditionController {

    private final EditionService editionService;
    private final UserService userService;
    private final ProductService productService;
    private final PreferenceService preferenceService;

    public EditionController(EditionService editionService, UserService userService, ProductService productService, PreferenceService preferenceService) {
        this.editionService = editionService;
        this.userService = userService;
        this.productService = productService;
        this.preferenceService = preferenceService;
    }

    @PostMapping(editionsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody ApiResponse createEdition(@CurrentUser UserPrincipal currentUser,
                                                   @Valid @RequestBody EditionTO editionTO) {
        return editionService.createEdition(editionTO, currentUser.getId());
    }

    @GetMapping(editionsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody List<EditionTO> getEditions() {
        return editionService.getEditions();
    }

    @PutMapping(editionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody ApiResponse editEdition(@CurrentUser UserPrincipal currentUser,
                                         @Valid @RequestBody EditionTO editionTO,
                                         @PathVariable("editionId") Long editionId) {
        return editionService.editEdition(editionTO, editionId, currentUser.getId());
    }

    @PostMapping(editionParticipantsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse joinEdition(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable("editionId") Long editionId) {
        return userService.joinEdition(currentUser.getId(), editionId);
    }

    @PutMapping(editionProductPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse assignProductToEdition(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable Long editionId,
                                                            @PathVariable Long productId) {
        return productService.assignProductToEdition(currentUser.getId(), editionId, productId);
    }

    @GetMapping(editionProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody Set<ProductTO> getProductsFromEdition(@CurrentUser UserPrincipal currentUser,
                                                               @PathVariable Long editionId) {
        return productService.getProductsFromEdition(currentUser.getId(), editionId);
    }

    @GetMapping(editionMyProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody Set<ProductTO> getMyProductsFromEdition(@CurrentUser UserPrincipal currentUser,
                                                                 @PathVariable Long editionId) {
        return productService.getMyProductsFromEdition(currentUser.getId(), editionId);
    }

    @GetMapping(editionModeratorsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody ApiResponse makeUserEditionModerator(@CurrentUser UserPrincipal currentUser,
                                                              @PathVariable Long editionId,
                                                              @Valid @RequestBody String username) {
        return editionService.makeUserEditionModerator(currentUser.getId(), editionId, username);
    }

    @PostMapping("/{editonId}/preferences")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addPreference(@CurrentUser UserPrincipal currentUser,
                                           @RequestBody Long haveProductId, Long wantProductId,
                                           @RequestParam Long editionId) {
        ApiResponse apiResponse = preferenceService.addPreference(currentUser.getId(), haveProductId, wantProductId, editionId);
        // to do gdzie ten util
    }

    @PostMapping("/{editonId}/groupPreferences")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addGroupPreference(@CurrentUser UserPrincipal currentUser,
                                                @RequestBody Long haveGroupId, Long wantProductId,
                                                @RequestParam Long editionId)  {
        ApiResponse apiResponse = preferenceService.addGroupPreference(currentUser.getId(), haveGroupId, wantProductId, editionId);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);

    }

    @PostMapping("/{editonId}/preferences")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyPreferencesFromOneEdtion(@CurrentUser UserPrincipal currentUser,
                                                           @RequestParam Long editionId) {
        preferenceService.getUserPreferencesFromOneEdtion(currentUser.getId(), editionId);
    }


}
