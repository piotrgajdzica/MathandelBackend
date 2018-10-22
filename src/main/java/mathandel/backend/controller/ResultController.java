package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.RateTO;
import mathandel.backend.model.client.ResultTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ResultService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ResultController {

    private ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping(resultsRequestsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse rateResult(@CurrentUser UserPrincipal currentUser, @RequestBody RateTO rateTO) {
        return resultService.rateResult(currentUser.getId(), rateTO);
    }

    @GetMapping(resultsRequestsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<RateTO> getUserRates(@CurrentUser UserPrincipal currentUser) {
        return resultService.getUserRates(currentUser.getId());
    }

    @GetMapping(resultsProductsToSendByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionProductsToSendForUser(@CurrentUser UserPrincipal currentUser, @RequestParam Long editionId) {
        return resultService.getEditionProductsToSendForUser(currentUser.getId(), editionId);
    }

    @GetMapping(resultsProductsToReceiveByUserPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ResultTO> getEditionProductsToReceiveForUser(@CurrentUser UserPrincipal currentUser, @RequestParam Long editionId) {
        return resultService.getEditionProductsToReceiveForUser(currentUser.getId(), editionId);
    }


}
