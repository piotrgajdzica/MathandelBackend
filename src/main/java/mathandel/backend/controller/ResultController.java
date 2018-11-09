package mathandel.backend.controller;

import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.client.TransactionRateTO;
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
    ApiResponse rateResult(@CurrentUser UserPrincipal currentUser, @RequestBody TransactionRateTO transactionRateTO) {
        return resultService.rateResult(currentUser.getId(), transactionRateTO);
    }

    @GetMapping(userRatesRequestPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<TransactionRateTO> getUserRates(@PathVariable Long userId) {
        return resultService.getUserRates(userId);
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
