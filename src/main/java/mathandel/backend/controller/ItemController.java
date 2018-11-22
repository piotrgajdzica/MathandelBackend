package mathandel.backend.controller;

import com.google.gson.Gson;
import mathandel.backend.model.client.ItemTO;
import mathandel.backend.model.client.request.CreateUpdateItemRequest;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // documented
    @PostMapping(editionItemsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO createItem(@CurrentUser UserPrincipal user,
                      @PathVariable Long editionId,
                      @RequestParam(value = "1", required = false) MultipartFile image1,
                      @RequestParam(value = "2", required = false) MultipartFile image2,
                      @RequestParam(value = "3", required = false) MultipartFile image3,
                      @RequestParam("item") String itemString) {
        return itemService.createItem(user.getId(), editionId, new Gson().fromJson(itemString, CreateUpdateItemRequest.class), image1, image2, image3);
    }

    // documented
    @PutMapping(itemPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ItemTO editItem(@CurrentUser UserPrincipal user,
                    @PathVariable Long itemId,
                    @RequestParam(value = "1", required = false) MultipartFile image1,
                    @RequestParam(value = "2", required = false) MultipartFile image2,
                    @RequestParam(value = "3", required = false) MultipartFile image3,
                    @RequestParam("item") String itemString) {
        return itemService.editItem(user.getId(), itemId, new Gson().fromJson(itemString, CreateUpdateItemRequest.class), image1, image2, image3);
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
