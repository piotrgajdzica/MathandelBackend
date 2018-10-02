package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.*;
import mathandel.backend.payload.request.CreateEditProductRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse createProduct(Long userId, CreateEditProductRequest createEditProductRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));

        Product product = new Product()
                .setName(createEditProductRequest.getName())
                .setDescription(createEditProductRequest.getDescription())
                .setUser(user);

        productRepository.save(product);

        return new ApiResponse(true, "Product created successfully.");
    }

    public ApiResponse editProduct(Long userId, CreateEditProductRequest createEditProductRequest, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException("Product doesn't exist."));

        if (!user.getId().equals(product.getUser().getId())) {
            throw new AppException("You have no access to this resource");
        }

        Edition edition = product.getEdition();
        if(edition != null) {
            EditionStatus editionStatus = edition.getEditionStatus();
            if(editionStatus.getEditionStatusName() != EditionStatusName.OPENED){
                throw new AppException("Product's edition is not opened.");
            }
        }

        product.setDescription(createEditProductRequest.getDescription())
                .setName(createEditProductRequest.getName());

        productRepository.save(product);
        return new ApiResponse(true, "Product edited successfully");
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new AppException("Product doesn't exist."));
    }
}
