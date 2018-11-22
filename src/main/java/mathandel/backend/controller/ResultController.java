package mathandel.backend.controller;

import mathandel.backend.model.client.ResultTO;
import mathandel.backend.model.client.ResultsTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ResultService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.editionModeratorResultsPath;
import static mathandel.backend.utils.UrlPaths.editionUserResultsPath;

@Controller
public class ResultController {

    private ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping(editionUserResultsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ResultsTO getEditionResultsForUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long editionId) {
        return resultService.getEditionResultsForUser(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(editionModeratorResultsPath)
    @PreAuthorize("hasRole('MODERATOR')")
    public @ResponseBody
    Set<ResultTO> getEditionResultsForModerator(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable Long editionId) {
        return resultService.getEditionResultsForModerator(currentUser.getId(), editionId);
    }
}
