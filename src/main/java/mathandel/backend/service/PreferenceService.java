package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static mathandel.backend.utils.ServerToClientDataConverter.mapPreference;
import static mathandel.backend.utils.ServerToClientDataConverter.mapPreferences;

//todo review and refactor
@Service
public class PreferenceService {

    private ProductRepository productRepository;
    private PreferenceRepository preferenceRepository;
    private EditionRepository editionRepository;
    private UserRepository userRepository;
    private DefinedGroupRepository definedGroupRepository;

    public PreferenceService(ProductRepository productRepository, PreferenceRepository preferenceRepository, EditionRepository editionRepository, UserRepository userRepository, DefinedGroupRepository definedGroupRepository) {
        this.productRepository = productRepository;
        this.preferenceRepository = preferenceRepository;
        this.editionRepository = editionRepository;
        this.userRepository = userRepository;
        this.definedGroupRepository = definedGroupRepository;
    }

    public PreferenceTO updatePreference(Long userId, PreferenceTO preferenceTO, Long editionId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));
        Product haveProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }
        if (!haveProduct.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not an owner of this product");
        }

        Optional<Preference> optionalPreference = preferenceRepository.findByHaveProduct_Id(productId);
        Preference preference = optionalPreference.orElseGet(() -> new Preference()
                .setHaveProduct(haveProduct)
                .setUser(user)
                .setEdition(edition));

        Set<Product> products = new HashSet<>(productRepository.findAllById(preferenceTO.getWantedProductsIds()));
        Set<DefinedGroup> groups = new HashSet<>(definedGroupRepository.findAllById(preferenceTO.getWantedDefinedGroupsIds()));

        for (Product product : products) {
            if (product.getUser().getId().equals(userId)) {
                throw new BadRequestException("User is the owner of product " + product.getId());
            }
        }

        for (DefinedGroup definedGroup : groups) {
            if (!definedGroup.getUser().getId().equals(userId)) {
                throw new BadRequestException("User is not the owner of group " + definedGroup.getId());
            }
        }

        preference.setWantedProducts(products);
        preference.setWantedDefinedGroups(groups);

        return mapPreference(preferenceRepository.save(preference));
    }

    public Set<PreferenceTO> getUserPreferencesFromOneEdition(Long userId, Long editionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User doesn't exist."));
        Edition edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId));

        if (edition.getEditionStatusType().getEditionStatusName() != EditionStatusName.OPENED) {
            throw new BadRequestException("Edition is not opened");
        }
        if (!edition.getParticipants().contains(user)) {
            throw new BadRequestException("User is not in this edition");
        }

        return mapPreferences(preferenceRepository.findAllByUser_IdAndEdition_Id(userId, editionId));
    }

    public PreferenceTO getPreferenceForProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not allowed to know other's user products preferences");
        }

        Preference preference = preferenceRepository.findByHaveProduct_Id(productId).orElseThrow(
                () -> new ResourceNotFoundException("Preference", "productId", productId));

        return mapPreference(preference);
    }
}
