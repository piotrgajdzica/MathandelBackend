package mathandel.backend.service;

import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.server.Preference;
import mathandel.backend.model.server.Product;
import mathandel.backend.repository.*;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

//todo review and refactor
@Service
public class PreferenceService {

    private ProductRepository productRepository;
    private PreferenceRepository preferenceRepository;
    private EditionRepository editionRepository;
    private UserRepository userRepository;
    private DefinedGroupRepository definedGroupRepository;

    @Autowired
    public PreferenceService(ProductRepository productRepository, PreferenceRepository preferenceRepository, EditionRepository editionRepository, UserRepository userRepository, DefinedGroupRepository definedGroupRepository) {
        this.productRepository = productRepository;
        this.preferenceRepository = preferenceRepository;
        this.editionRepository = editionRepository;
        this.userRepository = userRepository;
        this.definedGroupRepository = definedGroupRepository;
    }

    public ApiResponse addEditPreference(Long userId, PreferenceTO preferenceTO, Long editionId) {

        Product haveProduct = productRepository.findById(preferenceTO.getHaveProductId()).orElseThrow(() -> new ResourceNotFoundException("Product", "id", preferenceTO.getHaveProductId()));

        if (!haveProduct.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not an owner of the first product");
        }

        Preference preference;
        if (preferenceRepository.findByHaveProduct_Id(preferenceTO.getHaveProductId()) == null) {
            preference = new Preference()
                    .setHaveProduct(haveProduct)
                    .setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)))
                    .setEdition(editionRepository.findById(editionId).orElseThrow(() -> new ResourceNotFoundException("Edition", "id", editionId)));


        } else {
            preference = preferenceRepository.findByHaveProduct_Id(preferenceTO.getHaveProductId());
        }

        for (Long wantGroupId : preferenceTO.getWantedDefinedGroupsIds()) {
            preference.getWantedDefinedGroups().add(definedGroupRepository.findById(wantGroupId).orElseThrow(() -> new ResourceNotFoundException("Group", "id", wantGroupId)));
        }

        for (Long wantProductId : preferenceTO.getWantedProductsIds()) {
            preference.getWantedProducts().add(productRepository.findById(wantProductId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", wantProductId)));
        }

        preferenceRepository.save(preference);

        return new ApiResponse("Preference saved");
    }

    public Set<PreferenceTO> getUserPreferencesFromOneEdtion(Long userId, Long editionId) {
        return preferenceRepository.findAllByUser_IdAndEdition_Id(userId, editionId).stream().map(ServerToClientDataConverter::mapPreference).collect(Collectors.toSet());
    }

    public Set<PreferenceTO> getPreferencesForProduct(Long userId, Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if(!product.getUser().getId().equals(userId)){
            throw new BadRequestException("User is not allowed to know other's user products preferences");
        }

        Preference havePreference =  preferenceRepository.findByHaveProduct_Id(productId);

        Set<Product> products = new HashSet<Product>();
        products.add(product);

        Set<Preference> wantPreferences = preferenceRepository.findAllByWantedProductsContains(products);

        wantPreferences.add(havePreference);

        return ServerToClientDataConverter.mapPreferences(wantPreferences);
    }
}
