package mathandel.backend.service;

import mathandel.backend.model.client.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static mathandel.backend.utils.ServerToClientDataConverter.mapProduct;
import static mathandel.backend.utils.ServerToClientDataConverter.mapProducts;


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

    //todo set images
    public ProductTO createProduct(Long userId, Long editionId, ProductTO productTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }

        Product product = new Product()
                .setName(productTO.getName())
                .setDescription(productTO.getDescription())
                .setUser(user)
                .setEdition(edition);

        return mapProduct(productRepository.save(product));
    }

    //todo if preferences exists shouldnt be able to do this
    public ProductTO editProduct(Long userId, ProductTO productTO, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("You have no access to this resource");
        }

        Edition edition = product.getEdition();
        if (edition != null) {
            EditionStatusType editionStatusType = edition.getEditionStatusType();
            if (editionStatusType.getEditionStatusName() != EditionStatusName.OPENED) {
                throw new BadRequestException("Product's edition is not opened");
            }
        }

        product.setDescription(productTO.getDescription())
                .setName(productTO.getName());

        return mapProduct(productRepository.save(product));
    }

    public ProductTO getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return mapProduct(product);
    }

    public ApiResponse assignProductToEdition(Long userId, Long editionId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User doesn't exist."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
        if (!userId.equals(product.getUser().getId())) {
            throw new BadRequestException("You have no access to this product");
        }
        if (product.getEdition() != null) {
            throw new BadRequestException("Product already in edition");
        }

        product.setEdition(edition);
        productRepository.save(product);
        return new ApiResponse("Product successfully assigned to edition");

    }

    public Set<ProductTO> getNotAssignedProducts(Long userId) {
        return mapProducts(productRepository.findByUser_IdAndEditionIsNull(userId));
    }

    public Set<ProductTO> getProductsFromEdition(Long userId, Long editionId) {
        if (editionRepository.existsById(editionId)) {
            return mapProducts(productRepository.findByEdition_IdAndUser_IdNot(editionId, userId));
        } else {
            throw new ResourceNotFoundException("Edition", "id", editionId);
        }
    }

    public Set<ProductTO> getMyProductsFromEdition(Long userId, Long editionId) {
        if (editionRepository.existsById(editionId)) {
            return mapProducts(productRepository.findByEdition_IdAndUser_Id(editionId, userId));
        } else {
            throw new ResourceNotFoundException("Edition", "id", editionId);
        }
    }
}
