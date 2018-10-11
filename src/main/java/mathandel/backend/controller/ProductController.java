package mathandel.backend.controller;

import mathandel.backend.client.model.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseStatus(OK)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ProductTO createProduct(@CurrentUser UserPrincipal user, @RequestBody ProductTO productTO) {
        return productService.createProduct(user.getId(), productTO);
    }

    @ResponseStatus(OK)
    @PutMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ApiResponse editProduct(@CurrentUser UserPrincipal user, @RequestBody ProductTO productTO, @PathVariable Long productId) {
        return productService.editProduct(user.getId(), productTO, productId);

    }

    @ResponseStatus(OK)
    @GetMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody ProductTO getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @ResponseStatus(OK)
    @GetMapping("/not-assigned")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody Set<ProductTO> getNotAssignedProducts(@CurrentUser UserPrincipal current) {
        return productService.getNotAssignedProducts(current.getId());
    }

}
