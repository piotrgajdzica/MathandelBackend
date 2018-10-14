package mathandel.backend.service;

import mathandel.backend.client.model.DefinedGroupTO;
import mathandel.backend.client.model.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.DefinedGroup;
import mathandel.backend.model.Edition;
import mathandel.backend.model.Product;
import mathandel.backend.model.User;
import mathandel.backend.repository.DefinedGroupRepository;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static mathandel.backend.service.ProductService.mapProducts;

@Service
public class DefinedGroupService {

    private final UserRepository userRepository;
    private final EditionRepository editionRepository;
    private final DefinedGroupRepository definedGroupRepository;
    private final ProductRepository productRepository;

    public DefinedGroupService(UserRepository userRepository, EditionRepository editionRepository, DefinedGroupRepository definedGroupRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.productRepository = productRepository;
    }

    public ApiResponse createDefinedGroup(Long userId, Long editionId, DefinedGroupTO definedGroupTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        String groupName = definedGroupTO.getName();

        if(definedGroupRepository.existsByNameAndUser_IdAndEdition_Id(groupName, userId, editionId)) {
            throw new BadRequestException("You already have a definedGroup named \"" + groupName + "\" in edition " + edition.getName());
        }
        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("You are not in this edition");
        }

        DefinedGroup definedGroup = new DefinedGroup()
                .setName(groupName)
                .setUser(user)
                .setEdition(edition);

        definedGroupRepository.save(definedGroup);
        return new ApiResponse("DefinedGroup created successfully");
    }

    public ApiResponse editDefinedGroup(Long userId, Long editionId, Long groupId, DefinedGroupTO definedGroupTO) {
        DefinedGroup definedGroup = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));

        if(!definedGroup.getEdition().getId().equals(editionId)) {
            throw new BadRequestException("Group " + groupId + " is not in edition " + editionId);
        }
        if(!definedGroup.getUser().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not creator of group " + groupId);
        }

        definedGroup.setName(definedGroupTO.getName());
        definedGroupRepository.save(definedGroup);

        return new ApiResponse("Successfully edited group");
    }

    public Set<DefinedGroupTO> getDefinedGroupsFromEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("User does not exist"));
        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if(!edition.getParticipants().contains(user)) {
            throw new BadRequestException("You are not in this edition");
        }

        Set<DefinedGroup> definedGroups = definedGroupRepository.findByUser_IdAndEdition_Id(userId, editionId);

        return definedGroups.stream().map(DefinedGroupService::mapDefinedGroup).collect(Collectors.toSet());
    }

    public static DefinedGroupTO mapDefinedGroup(DefinedGroup definedGroup) {
        return new DefinedGroupTO()
                .setId(definedGroup.getId())
                .setName(definedGroup.getName())
                .setNumberOfProducts(definedGroup.getProducts().size());
    }

    public ApiResponse addProductToDefinedGroup(Long userId, Long editionId, Long groupId, Long productId) {
        DefinedGroup definedGroup = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if(!definedGroup.getEdition().getId().equals(editionId)) {
            throw new BadRequestException("Group " + groupId + " is not in edition " + editionId);
        }
        if(!definedGroup.getUser().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not creator of group " + groupId);
        }
        if(product.getEdition() == null) {
            throw new BadRequestException("Product is not assigned to any edition yet");
        }
        if(!product.getEdition().getId().equals(editionId)) {
            throw new BadRequestException("Product is not in the same edition as defined group");
        }
        if(product.getUser().getId().equals(userId)) {
            throw new BadRequestException("Product belongs to user");
        }
        if(definedGroup.getProducts().contains(product)) {
            throw new BadRequestException("Product already in this group");
        }

        definedGroup.getProducts().add(product);
        definedGroupRepository.save(definedGroup);

        return new ApiResponse("Product successfully added to group");
    }

    public Set<ProductTO> getNamedGroupProducts(Long userId, Long editionID, Long groupId) {
        DefinedGroup definedGroup = definedGroupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("DefinedGroup", "id", groupId));
        Set<Product> products = definedGroup.getProducts();

        if(!definedGroup.getUser().getId().equals(userId)) {
            throw new BadRequestException("User " + userId + " is not creator of group " + groupId);
        }

        return mapProducts(products);
    }
}
