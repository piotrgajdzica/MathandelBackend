package mathandel.backend.controller;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.client.TransactionRateTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.RateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.ratesPath;
import static mathandel.backend.utils.UrlPaths.userRatesPath;

@Controller
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping(ratesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ApiResponse rateResult(@CurrentUser UserPrincipal currentUser, @RequestBody TransactionRateTO transactionRateTO) {
        return rateService.rateResult(currentUser.getId(), transactionRateTO);
    }

    @GetMapping(userRatesPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<TransactionRateTO> getUserRates(@PathVariable Long userId) {
        return rateService.getUserRates(userId);
    }
}
