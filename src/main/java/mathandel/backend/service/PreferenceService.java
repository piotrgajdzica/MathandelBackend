package mathandel.backend.service;

import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.model.server.Preference;
import mathandel.backend.model.server.Product;
import mathandel.backend.repository.*;
import mathandel.backend.utils.ServerToClientDataConverter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PreferenceService {

    private ProductRepository productRepository;

    private PreferencesRepository preferencesRepository;

    private EditionRepository editionRepository;

    private UserRepository userRepositry;

    private DefinedGroupRepository definedGroupRepository;

    //todo constructor

    public ApiResponse addPreference(Long userId, Long haveProductId, Long wantProductId, Long editionId) {

        Product haveProduct = productRepository.getOne(haveProductId);

        if (!haveProduct.getUser().getId().equals(userId)) {
            throw new BadRequestException("User is not an owner of the first product");
        }

        if (preferencesRepository.getByWantProduct_IdAndHaveProduct_Id(wantProductId, haveProductId) != null) {
            throw new BadRequestException("Preference already defined");
        }

        Preference preference = new Preference()
                .setHaveProduct(haveProduct)
                .setWantProduct(productRepository.getOne(wantProductId))
                .setUser(userRepositry.getOne(userId))
                .setEdition(editionRepository.getOne(editionId));

        preferencesRepository.save(preference);

        return new ApiResponse( "Preference saved");

    }

    public ApiResponse addGroupPreference(Long userId, Long haveGroupId, Long wantProductId, Long editionId) {

        if (preferencesRepository.getByWantProduct_IdAndDefinedGroup_Id(wantProductId, haveGroupId) != null) {
            throw new BadRequestException("Preference already defined");
        }

        Preference preference = new Preference()
                .setDefinedGroup(definedGroupRepository.getOne(haveGroupId))
                .setWantProduct(productRepository.getOne(wantProductId))
                .setUser(userRepositry.getOne(userId))
                .setEdition(editionRepository.getOne(editionId));

        preferencesRepository.save(preference);

        return new ApiResponse( "Preference saved");
    }

    public Set<PreferenceTO> getUserPreferencesFromOneEdtion(Long userId, Long editionId) {
        return preferencesRepository.findAllByUser_IdAndEdition_Id(userId, editionId).stream().map(ServerToClientDataConverter::mapPreference).collect(Collectors.toSet()) ;
    }
}
