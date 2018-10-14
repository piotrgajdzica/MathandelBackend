package mathandel.backend.controller;

import mathandel.backend.client.model.PreferenceTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.DefinedGroupService;
import mathandel.backend.service.EditionService;
import mathandel.backend.service.PreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/preferences/{preferenceId}/rate")
public class PreferenceControler {

    PreferenceService preferenceService;

    EditionService editionService;

    DefinedGroupService definedGruoupService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> rateExchange
            (@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody PreferenceTO preference) {
        // todo
    }

}
