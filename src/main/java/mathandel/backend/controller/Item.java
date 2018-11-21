package mathandel.backend.controller;

import mathandel.backend.model.client.ItemTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.PreferenceService;
import mathandel.backend.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class Item {

    private ItemService itemService;
    private PreferenceService preferenceService;

    public Item(ItemService itemService, PreferenceService preferenceService) {
        this.itemService = itemService;
        this.preferenceService = preferenceService;
    }

    // documented
    @PostMapping(editionItemsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO createItem(@CurrentUser UserPrincipal user,
                      @PathVariable Long editionId,
                      @RequestBody ItemTO itemTO) {
        return itemService.createItem(user.getId(), editionId, itemTO);
    }

    // documented
    @PutMapping(itemPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO editItem(@CurrentUser UserPrincipal user,
                    @RequestBody ItemTO itemTO,
                    @PathVariable Long itemId) {
        return itemService.editItem(user.getId(), itemTO, itemId);

    }

    // documented
    @GetMapping(itemPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    // documented
    @PutMapping(editionItemPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO assignItemToEdition(@CurrentUser UserPrincipal currentUser,
                               @PathVariable Long editionId,
                               @PathVariable Long itemId) {
        return itemService.assignItemToEdition(currentUser.getId(), editionId, itemId);
    }

    // documented
    @GetMapping(editionItemsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ItemTO> getItemsFromEdition(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long editionId) {
        return itemService.getItemsFromEdition(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(editionMyItemsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ItemTO> getMyItemsFromEdition(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable Long editionId) {
        return itemService.getMyItemsFromEdition(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(notAssignedItemsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ItemTO> getNotAssignedItems(@CurrentUser UserPrincipal current) {
        return itemService.getNotAssignedItems(current.getId());
    }
}
