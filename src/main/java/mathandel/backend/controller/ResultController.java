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

import static mathandel.backend.utils.UrlPaths.editionResultsPath;
import static mathandel.backend.utils.UrlPaths.resultsItemsToReceiveByUserPath;
import static mathandel.backend.utils.UrlPaths.resultsItemsToSendByUserPath;

@Controller
public class ResultController {

    private ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    // documented
    @GetMapping(resultsItemsToSendByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionItemsToSendForUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return resultService.getEditionItemsToSendForUser(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(resultsItemsToReceiveByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionItemsToReceiveForUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return resultService.getEditionItemsToReceiveForUser(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(editionResultsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    Set<ResultTO> getEditionResults(@CurrentUser UserPrincipal currentUser,
                                  @PathVariable Long editionId) {
        return resultService.getEditionResults(currentUser.getId(), editionId);
    }
}
