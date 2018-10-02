package mathandel.backend.controller;

import mathandel.backend.payload.request.CreateEditProductRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.security.CurrentUser;
import mathandel.backend.security.UserPrincipal;
import mathandel.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createProduct(@CurrentUser UserPrincipal user, CreateEditProductRequest createEditProductRequest) {
        ApiResponse apiResponse = productService.createProduct(user.getId(), createEditProductRequest);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @PutMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editProduct(@CurrentUser UserPrincipal user, CreateEditProductRequest createEditProductRequest, @PathVariable Long productId) {
        ApiResponse apiResponse = productService.editProduct(user.getId(), createEditProductRequest, productId);
        return apiResponse.getSuccess() ? ResponseEntity.ok(apiResponse) : ResponseEntity.badRequest().body(apiResponse);
    }

    @GetMapping("{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
}
