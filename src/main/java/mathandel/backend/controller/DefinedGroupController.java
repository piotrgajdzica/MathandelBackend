package mathandel.backend.controller;

import mathandel.backend.model.client.DefinedGroupContentTO;
import mathandel.backend.model.client.DefinedGroupTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.DefinedGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class DefinedGroupController {

    private final DefinedGroupService definedGroupService;

    public DefinedGroupController(DefinedGroupService definedGroupService) {
        this.definedGroupService = definedGroupService;
    }

    // documented
    @PostMapping(definedGroups)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    DefinedGroupTO createDefinedGroup(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long editionId,
                                   @Valid @RequestBody DefinedGroupTO definedGroupTO) {
        return definedGroupService.createDefinedGroup(currentUser.getId(), editionId, definedGroupTO);
    }

    // documented
    @PutMapping(definedGroup)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    DefinedGroupTO editDefinedGroup(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long editionId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody DefinedGroupTO definedGroupTO) {
        return definedGroupService.editDefinedGroup(currentUser.getId(), editionId, groupId, definedGroupTO);
    }

    // documented
    @GetMapping(definedGroups)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<DefinedGroupTO> getDefinedGroupsFromEdition(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long editionId) {
        return definedGroupService.getDefinedGroupsFromEdition(currentUser.getId(), editionId);
    }

    // documented
    @PostMapping(definedGroupContent)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    DefinedGroupTO updateDefinedGroupContent(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable Long editionId,
                                          @PathVariable Long groupId,
                                          @Valid @RequestBody DefinedGroupContentTO definedGroupContentTO) {
        return definedGroupService.updateDefinedGroup(currentUser.getId(), editionId, groupId, definedGroupContentTO);
    }

    // documented
    @GetMapping(definedGroup)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    DefinedGroupTO getDefinedGroup(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long editionId,
                                   @PathVariable Long groupId) {
        return definedGroupService.getDefinedGroup(currentUser.getId(), editionId, groupId);
    }
}
