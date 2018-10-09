package mathandel.backend.service;

import mathandel.backend.client.model.ProductTO;
import mathandel.backend.client.request.ProductDataRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.Product;
import mathandel.backend.model.User;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


//todo it tests
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

    public ProductTO createProduct(Long userId, ProductDataRequest productDataRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));

        Product product = new Product()
                .setName(productDataRequest.getName())
                .setDescription(productDataRequest.getDescription())
                .setUser(user);

        return mapProduct(productRepository.save(product));
    }

    //todo if preferences exists shouldnt be able to do this maybe should only be able to add photos
    public ApiResponse editProduct(Long userId, ProductDataRequest productDataRequest, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Product product = productRepository.findById(productId).orElse(null);

        if(product == null) {
            return new ApiResponse(false, "Product doesn't exist");
        }

        if (!user.getId().equals(product.getUser().getId())) {
            return new ApiResponse(false, "You have no access to this resource");
        }

        Edition edition = product.getEdition();
        if (edition != null) {
            EditionStatusType editionStatusType = edition.getEditionStatusType();
            if (editionStatusType.getEditionStatusName() != EditionStatusName.OPENED) {
                return new ApiResponse(false,"Product's edition is not opened");
            }
        }

        product.setDescription(productDataRequest.getDescription())
                .setName(productDataRequest.getName());

        productRepository.save(product);
        return new ApiResponse(true, "Product edited successfully");
    }

    public ProductTO getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            return mapProduct(product);
        } else {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
    }

    public ApiResponse assignProductToEdition(Long userId, Long editionId, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        Edition edition = editionRepository.findById(editionId).orElse(null);

        if(product == null) {
            return new ApiResponse(false, "Product doesn't exist");
        }
        if(edition == null) {
            return new ApiResponse(false,"Edition doesn't exist");
        }
        if(edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            return new ApiResponse(false, "Edition isn't opened");
        }
        if (!userId.equals(product.getUser().getId())) {
            return new ApiResponse(false, "You have no access to this product");
        }
        if (product.getEdition() != null) {
            return new ApiResponse(false, "Product already in edition");
        }

        product.setEdition(edition);
        productRepository.save(product);
        return new ApiResponse(true, "Product successfully assigned to edition");

    }

    public Set<ProductTO> getNotAssignedProducts(Long userId) {
        return mapProducts(productRepository.findByUser_IdAndEditionIsNull(userId));
    }

    public Set<ProductTO> getProductsFromEdition(Long userId, Long editionId) {
        if(editionRepository.existsById(editionId)) {
            return mapProducts(productRepository.findByEdition_IdAndUser_IdNot(editionId, userId));
        } else {
            throw new ResourceNotFoundException("Edition", "id", editionId);
        }
    }

    public Set<ProductTO> getMyProductsFromEdition(Long userId, Long editionId) {
        if(editionRepository.existsById(editionId)) {
            return mapProducts(productRepository.findByEdition_IdAndUser_Id(editionId, userId));
        } else {
            throw new ResourceNotFoundException("Edition", "id", editionId);
        }
    }

    private ProductTO mapProduct(Product product) {
        return new ProductTO()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setUserId(product.getUser().getId());

    }

    private Set<ProductTO> mapProducts(Set<Product> products) {
        return products.stream().map(this::mapProduct).collect(Collectors.toSet());
    }
}
