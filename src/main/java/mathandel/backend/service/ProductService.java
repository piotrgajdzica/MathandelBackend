package mathandel.backend.service;

import mathandel.backend.client.model.ProductTO;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.*;
import mathandel.backend.client.request.ProductDataRequest;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


//todo tests and it tests
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
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));

        Product product = new Product()
                .setName(productDataRequest.getName())
                .setDescription(productDataRequest.getDescription())
                .setUser(user);

        productRepository.save(product);

        return mapProduct(product);
    }

    //todo if preferences exists shouldnt be able to do this maybe should be able to add photos
    public ApiResponse editProduct(Long userId, ProductDataRequest productDataRequest, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(!optionalProduct.isPresent()) {
            return new ApiResponse(false, "Product doesn't exist.");
        }

        Product product = optionalProduct.get();

        if (!user.getId().equals(product.getUser().getId())) {
            return new ApiResponse(false, "You have no access to this resource");
        }

        Edition edition = product.getEdition();
        if (edition != null) {
            EditionStatusType editionStatusType = edition.getEditionStatusType();
            if (editionStatusType.getEditionStatusName() != EditionStatusName.OPENED) {
                throw new AppException("Product's edition is not opened.");
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
        if (!userId.equals(product.getUser().getId())) {
            return new ApiResponse(false, "You have no access to this product");
        }
        if (product.getEdition() != null) {
            return new ApiResponse(false, "Product already in another edition");
        }

        product.setEdition(edition);
        productRepository.save(product);
        return new ApiResponse(true, "Product successfully assigned to edition");

    }

    public Set<ProductTO> getNotAssignedProducts(Long userId) {
        return mapProducts(productRepository.findByUser_IdAndEditionIsNull(userId));
    }

    //todo problem with using edition ids is that when edition doesnt exist you get just empty list with 200
    public Set<ProductTO> getProductsFromEdition(Long userId, Long editionId) {
        return mapProducts(productRepository.findByEdition_IdAndUser_IdNot(editionId, userId));
    }

    //todo problem with using edition ids is that when edition doesnt exist you get just empty list with 200
    public Set<ProductTO> getMyProductsFromEdition(Long userId, Long editionId) {
        return mapProducts(productRepository.findByEdition_IdAndUser_Id(editionId, userId));
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
