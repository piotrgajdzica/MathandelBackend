package mathandel.backend.controller;

import mathandel.backend.model.client.ProductTO;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ImageService;
import mathandel.backend.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static mathandel.backend.utils.UrlPaths.*;

@Controller
public class ProductController {

    private ProductService productService;
    private ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @PostMapping(productsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ProductTO createProduct(@CurrentUser UserPrincipal user,
                                                 @RequestBody ProductTO productTO) {
        return productService.createProduct(user.getId(), productTO);
    }

    @PutMapping(productPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse editProduct(@CurrentUser UserPrincipal user,
                                                 @RequestBody ProductTO productTO,
                                                 @PathVariable Long productId) {
        return productService.editProduct(user.getId(), productTO, productId);

    }

    @GetMapping(productPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ProductTO getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @GetMapping(notAssignedProductsPath)
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody Set<ProductTO> getNotAssignedProducts(@CurrentUser UserPrincipal current) {
        return productService.getNotAssignedProducts(current.getId());
    }
}
