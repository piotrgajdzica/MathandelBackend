package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.*;
import mathandel.backend.payload.request.CreateEditProductRequest;
import mathandel.backend.payload.response.ApiResponse;
import mathandel.backend.payload.response.ProductResponse;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, EditionRepository editionRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
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

    //todo if preferences exists shouldnt be able to do this maybe should be able to add photos
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

    public ApiResponse assignProductToEdition(Long userId, Long editionId, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException("Product doesn't exist."));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist."));

        if(!userId.equals(product.getUser().getId())){
            return new ApiResponse(false, "You have no access to this product.");
        }

        if(product.getEdition() != null){
            return new ApiResponse(false, "Product already in another edition.");
        }

        product.setEdition(edition);
        productRepository.save(product);
        return new ApiResponse(true, "Product successfully assigned to edition.");

    }

    public Set<Product> getNotAssignedProducts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        return productRepository.findByUserAndEditionIsNull(user);
    }

    public Set<Product> getOthersProductsFromEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist."));
        return productRepository.findByEditionAndUserNot(edition, user);
    }

    public Set<Product> getMyProductsFromEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition doesn't exist."));
        return productRepository.findByEditionAndUser(edition, user);
    }
}
