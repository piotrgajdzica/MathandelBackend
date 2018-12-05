package mathandel.backend.controller;

import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.client.request.ReOpenEditionRequest;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.CalcService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class CalcController {

    private final CalcService calcService;

    public CalcController(CalcService calcService) {
        this.calcService = calcService;
    }

    // documented
    @PostMapping(resolveEditionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO resolveEdition(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long editionId) {
        return calcService.resolveEdition(userPrincipal.getId(), editionId);
    }

    // documented
    @PostMapping(publishEditionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO publishEdition(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long editionId) {
        return calcService.publishEdition(userPrincipal.getId(), editionId);
    }

    // documented
    @PostMapping(reOpenEditionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO reOpenEdition(@CurrentUser UserPrincipal userPrincipal,
                            @PathVariable Long editionId,
                            @RequestBody ReOpenEditionRequest reOpenEditionRequest) {
        return calcService.reOpenEdition(userPrincipal.getId(), editionId, reOpenEditionRequest.getEndDate());
    }

    // documented
    @PostMapping(cancelEditionPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    EditionTO cancelEdition(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long editionId) {
        return calcService.cancelEdition(userPrincipal.getId(), editionId);
    }

}
