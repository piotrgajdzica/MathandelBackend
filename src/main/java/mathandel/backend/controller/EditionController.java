package mathandel.backend.controller;

import mathandel.backend.model.client.request.MakeUserEditionModeratorRequest;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.server.Result;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.*;
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
    private final PreferenceService preferenceService;
    private final ResultService resultService;

    public EditionController(EditionService editionService, UserService userService, ProductService productService, PreferenceService preferenceService, ResultService resultService) {
        this.editionService = editionService;
        this.userService = userService;
        this.preferenceService = preferenceService;
        this.resultService = resultService;
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

    @PostMapping(editionPreferencesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse addEditPreference(@CurrentUser UserPrincipal currentUser,
                                  @RequestBody PreferenceTO preferenceTO,
                                  @PathVariable Long editionId) {

        return preferenceService.addEditPreference(currentUser.getId(), preferenceTO, editionId);
    }

    @GetMapping(editionPreferencesPath)
    @PreAuthorize("hasRole('USER')")
    public Set<PreferenceTO> getMyPreferencesFromOneEdition(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable Long editionId) {
        return preferenceService.getUserPreferencesFromOneEdtion(currentUser.getId(), editionId);
    }


    @GetMapping(editionResultsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    Set<Result> getEditionResults(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long editionId) {
        return resultService.getEditionResults(currentUser.getId(), editionId);
    }


}
