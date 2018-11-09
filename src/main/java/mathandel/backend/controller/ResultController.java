package mathandel.backend.controller;

import mathandel.backend.model.client.ResultTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ResultService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.resultsProductsToReceiveByUserPath;
import static mathandel.backend.utils.UrlPaths.resultsProductsToSendByUserPath;

@Controller
public class ResultController {

    private ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping(resultsProductsToSendByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionProductsToSendForUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return resultService.getEditionProductsToSendForUser(currentUser.getId(), editionId);
    }

    @GetMapping(resultsProductsToReceiveByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionProductsToReceiveForUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return resultService.getEditionProductsToReceiveForUser(currentUser.getId(), editionId);
    }
}
