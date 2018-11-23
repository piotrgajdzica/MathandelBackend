package mathandel.backend.controller;

import mathandel.backend.model.client.RateTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.RateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static mathandel.backend.utils.UrlPaths.ratePath;

@Controller
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    // documented
    @PostMapping(ratePath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    RateTO rateResult(@CurrentUser UserPrincipal currentUser,
                      @PathVariable Long resultId,
                      @RequestBody RateTO rateTO) {
        return rateService.rateResult(currentUser.getId(), resultId, rateTO);
    }

}
