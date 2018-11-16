package mathandel.backend.controller;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.client.ProductTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.PreferenceService;
import mathandel.backend.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ProductController {

    private ProductService productService;
    private PreferenceService preferenceService;

    public ProductController(ProductService productService, PreferenceService preferenceService) {
        this.productService = productService;
        this.preferenceService = preferenceService;
    }

    // documented
    @PostMapping(editionProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ProductTO createProduct(@CurrentUser UserPrincipal user,
                            @PathVariable Long editionId,
                            @RequestBody ProductTO productTO) {
        return productService.createProduct(user.getId(), editionId, productTO);
    }

    // documented
    @PutMapping(productPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ProductTO editProduct(@CurrentUser UserPrincipal user,
                          @RequestBody ProductTO productTO,
                          @PathVariable Long productId) {
        return productService.editProduct(user.getId(), productTO, productId);

    }

    // documented
    @GetMapping(productPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ProductTO getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    // documented
    @PutMapping(editionProductPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ProductTO assignProductToEdition(@CurrentUser UserPrincipal currentUser,
                                       @PathVariable Long editionId,
                                       @PathVariable Long productId) {
        return productService.assignProductToEdition(currentUser.getId(), editionId, productId);
    }

    // documented
    @GetMapping(editionProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ProductTO> getProductsFromEdition(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable Long editionId) {
        return productService.getProductsFromEdition(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(editionMyProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ProductTO> getMyProductsFromEdition(@CurrentUser UserPrincipal currentUser,
                                            @PathVariable Long editionId) {
        return productService.getMyProductsFromEdition(currentUser.getId(), editionId);
    }

    // documented
    @GetMapping(notAssignedProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    Set<ProductTO> getNotAssignedProducts(@CurrentUser UserPrincipal current) {
        return productService.getNotAssignedProducts(current.getId());
    }
}
