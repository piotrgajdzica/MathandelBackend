package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
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

    @PostMapping(definedGroups)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse createDefinedGroup(@CurrentUser UserPrincipal currentUser,
                                   @PathVariable Long editionId,
                                   @Valid @RequestBody DefinedGroupTO definedGroupTO) {
        return definedGroupService.createDefinedGroup(currentUser.getId(), editionId, definedGroupTO);
    }

    @PostMapping(definedGroup)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse editDefinedGroup(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long editionId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody DefinedGroupTO definedGroupTO) {
        return definedGroupService.editDefinedGroup(currentUser.getId(), editionId, groupId, definedGroupTO);
    }

    @GetMapping(definedGroups)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<DefinedGroupTO> getDefinedGroupsFromEdition(@CurrentUser UserPrincipal currentUser,
                                                    @PathVariable Long editionId) {
        return definedGroupService.getDefinedGroupsFromEdition(currentUser.getId(), editionId);
    }

    @PostMapping(definedGroupContent)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse updateDefinedGroupContent(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long editionId,
                                         @PathVariable Long groupId,
                                         @Valid @RequestBody DefinedGroupContentTO definedGroupContentTO) {
        return definedGroupService.updateDefinedGroupContent(currentUser.getId(), editionId, groupId, definedGroupContentTO);
    }

    @GetMapping(definedGroupContent)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    DefinedGroupContentTO getNamedGroupContent(@CurrentUser UserPrincipal currentUser,
                                         @PathVariable Long editionId,
                                         @PathVariable Long groupId) {
        return definedGroupService.getNamedGroupProducts(currentUser.getId(), editionId, groupId);
    }
}
