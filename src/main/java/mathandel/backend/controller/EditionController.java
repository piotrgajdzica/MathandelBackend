package mathandel.backend.controller;

import mathandel.backend.model.client.request.MakeUserEditionModeratorRequest;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.client.EditionTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class EditionController {

    private final EditionService editionService;
    private final UserService userService;
    private final PreferenceService preferenceService;
    private final ResultService resultService;

    public EditionController(EditionService editionService, UserService userService, ItemService itemService, PreferenceService preferenceService, ResultService resultService) {
        this.editionService = editionService;
        this.userService = userService;
        this.preferenceService = preferenceService;
        this.resultService = resultService;
    }

    // documented
    @PostMapping(editionsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO createEdition(@CurrentUser UserPrincipal currentUser,
                            @Valid @RequestBody EditionTO editionTO) {
        return editionService.createEdition(editionTO, currentUser.getId());
    }

    // documented
    @GetMapping(editionsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    List<EditionTO> getEditions(@CurrentUser UserPrincipal current) {
        return editionService.getEditions(current.getId());
    }

    // documented
    @PutMapping(editionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO editEdition(@CurrentUser UserPrincipal currentUser,
                          @Valid @RequestBody EditionTO editionTO,
                          @PathVariable Long editionId) {
        return editionService.editEdition(editionTO, editionId, currentUser.getId());
    }

    // documented
    @PostMapping(editionParticipantsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    EditionTO joinEdition(@CurrentUser UserPrincipal currentUser,
                            @PathVariable Long editionId) {
        return userService.joinEdition(currentUser.getId(), editionId);
    }

    // documented
    @PostMapping(editionModeratorsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    ApiResponse makeUserEditionModerator(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long editionId,
                                         @Valid @RequestBody MakeUserEditionModeratorRequest makeUserEditionModeratorRequest) {
        return editionService.makeUserEditionModerator(currentUser.getId(), editionId, makeUserEditionModeratorRequest.getUsername());
    }
}
