package mathandel.backend.controller;

import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.PreferenceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // documented
    @PostMapping(editionProductPreferencePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    PreferenceTO updatePreference(@CurrentUser UserPrincipal currentUser,
                                  @RequestBody PreferenceTO preferenceTO,
                                  @PathVariable Long editionId,
                                  @PathVariable Long productId) {
        return preferenceService.updatePreference(currentUser.getId(), preferenceTO, editionId, productId);
    }

    // documented
    @GetMapping(editionPreferencesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<PreferenceTO> getMyPreferencesFromOneEdition(@CurrentUser UserPrincipal currentUser,
                                                     @PathVariable Long editionId) {
        return preferenceService.getUserPreferencesFromOneEdition(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(preferenceForProductPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    PreferenceTO getPreferenceForProduct(@CurrentUser UserPrincipal current, @PathVariable Long productId) {
        return preferenceService.getPreferenceForProduct(current.getId(), productId);
    }
}
