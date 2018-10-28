package mathandel.backend.service;


import mathandel.backend.exception.AppException;
import mathandel.backend.model.server.*;
import mathandel.backend.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CalcService {

    PreferenceRepository preferenceRepository;

    DefinedGroupRepository definedGroupRepository;

    ResultRepository resultRepository;

    ProductRepository productRepository;

    EditionRepository editionRepository;


    public CalcService(PreferenceRepository preferenceReposiory, DefinedGroupRepository definedGroupRepository, ResultRepository resultRepository, ProductRepository productRepository, EditionRepository editionRepository) {
        this.productRepository = productRepository;
        this.editionRepository = editionRepository;
        this.preferenceRepository = preferenceRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.resultRepository = resultRepository;
    }


    // todo check this shit
    public void saveResultsFromJsonData(Long editionId, String jsonString) {

        JSONArray jsonData = new JSONArray(jsonString);

        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject node = jsonData.getJSONObject(i);

            resultRepository.save(getResult(editionId, node));
        }
    }


    private Result getResult(Long editionId, JSONObject node) {


        Long receiversProductId = node.getLong("receiver");
        Long productToSentId = node.getLong("sender");

        Product productToSent = productRepository.findById(productToSentId).orElseThrow(() -> new AppException("Product not found"));
        Product receiversProduct = productRepository.findById(receiversProductId).orElseThrow(() -> new AppException("Product not found"));

        User receiver = receiversProduct.getUser();
        User sender = productToSent.getUser();

        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition not found"));
        Result result = new Result()
                .setEdition(edition)
                .setProductToSend(productToSent)
                .setReceiver(receiver)
                .setSender(sender);
        return result;

    }

    public String getMappedDataForEdition(Long editionId) {
        Set<Preference> preferences = preferenceRepository.findAllByEdition_Id(editionId);
        Set<DefinedGroup> definedGroups = definedGroupRepository.findAllByEdition_Id(editionId);

        JSONObject mappedData = new JSONObject();
        JSONArray definedGrupsAsJsonTable = new JSONArray(definedGroups.stream().map(this::mapGroup).collect(Collectors.toList()));
        JSONArray preferencesAsJsonTable = new JSONArray(preferences.stream().map(this::mapPreference).collect(Collectors.toList()));

        return mappedData
                .put("named_groups", definedGrupsAsJsonTable)
                .put("preferences", preferencesAsJsonTable).toString();

    }

    private JSONObject mapGroup(DefinedGroup definedGroup) {
        return new JSONObject()
                .put("id", definedGroup.getId())
                .put("single_preferences", new JSONArray(definedGroup.getProducts().stream().map(Product::getId).collect(Collectors.toList())))
                .put("groups", new JSONArray(definedGroup.getDefinedGroups().stream().map(DefinedGroup::getId).collect(Collectors.toList())));

    }


    private JSONObject mapPreference(Preference preference) {
        return new JSONObject()
                .put("id", preference.getHaveProduct().getId())
                .put("single_preferences", new JSONArray(preference.getWantedProducts().stream().map(Product::getId).collect(Collectors.toList())))
                .put("groups", new JSONArray(preference.getWantedDefinedGroups().stream().map(DefinedGroup::getId).collect(Collectors.toList())));

    }
}
