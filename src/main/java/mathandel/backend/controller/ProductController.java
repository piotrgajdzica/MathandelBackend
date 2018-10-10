package mathandel.backend.controller;

import mathandel.backend.client.model.ProductTO;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static mathandel.backend.utils.ResponseCreator.createResponse;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createProduct(@CurrentUser UserPrincipal user, @RequestBody ProductTO productTO) {
        return ResponseEntity.ok(productService.createProduct(user.getId(), productTO));
    }

    @PutMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editProduct(@CurrentUser UserPrincipal user, @RequestBody ProductTO productTO, @PathVariable Long productId) {
        return createResponse(productService.editProduct(user.getId(), productTO, productId));

    }

    @GetMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/not-assigned")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getNotAssignedProducts(@CurrentUser UserPrincipal current) {
        return ResponseEntity.ok(productService.getNotAssignedProducts(current.getId()));
    }

}
