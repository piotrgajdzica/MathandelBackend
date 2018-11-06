package mathandel.backend.controller;

import mathandel.backend.client.request.MakeUserEditionModeratorRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.client.ProductTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.EditionService;
import mathandel.backend.service.PreferenceService;
import mathandel.backend.service.ProductService;
import mathandel.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
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
    public @ResponseBody
    EditionTO createEdition(@CurrentUser UserPrincipal currentUser,
                              @Valid @RequestBody EditionTO editionTO) {
        return editionService.createEdition(editionTO, currentUser.getId());
    }

    @GetMapping(editionsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    List<EditionTO> getEditions(@CurrentUser UserPrincipal current) {
        return editionService.getEditions(current.getId());
    }

    @PutMapping(editionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO editEdition(@CurrentUser UserPrincipal currentUser,
                            @Valid @RequestBody EditionTO editionTO,
                            @PathVariable Long editionId) {
        return editionService.editEdition(editionTO, editionId, currentUser.getId());
    }

    @PostMapping(editionParticipantsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse joinEdition(@CurrentUser UserPrincipal currentUser,
                            @PathVariable Long editionId) {
        return userService.joinEdition(currentUser.getId(), editionId);
    }

    @PostMapping(editionModeratorsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    ApiResponse makeUserEditionModerator(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long editionId,
                                         @Valid @RequestBody MakeUserEditionModeratorRequest makeUserEditionModeratorRequest) {
        return editionService.makeUserEditionModerator(currentUser.getId(), editionId, makeUserEditionModeratorRequest.getUsername());
    }

    //todo paths to utils
    //todo request body preferenceTO
    @PostMapping("/{editonId}/preferences")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse addPreference(@CurrentUser UserPrincipal currentUser,
                              @RequestBody Long haveProductId, Long wantProductId,
                              @RequestParam Long editionId) {
        return preferenceService.addPreference(currentUser.getId(), haveProductId, wantProductId, editionId);
    }

    //todo the same as above
    @PostMapping("/{editonId}/groupPreferences")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse addGroupPreference(@CurrentUser UserPrincipal currentUser,
                                   @RequestBody Long haveGroupId, Long wantProductId,
                                   @RequestParam Long editionId) {
        return preferenceService.addGroupPreference(currentUser.getId(), haveGroupId, wantProductId, editionId);
    }

    //todo rename typo
    @GetMapping("/{editonId}/preferences")
    @PreAuthorize("hasRole('USER')")
    public Set<PreferenceTO> getMyPreferencesFromOneEdtion(@CurrentUser UserPrincipal currentUser,
                                                           @RequestParam Long editionId) {
        return preferenceService.getUserPreferencesFromOneEdtion(currentUser.getId(), editionId);
    }


}
