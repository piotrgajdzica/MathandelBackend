package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.response.ApiResponse;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.stream.Collectors;

//todo review and refactor
@Service
public class CalcService {

    private PreferenceRepository preferenceRepository;
    private DefinedGroupRepository definedGroupRepository;
    private ResultRepository resultRepository;
    private ProductRepository productRepository;
    private EditionRepository editionRepository;
    private final EditionService editionService;
    private final RestTemplate restTemplate;

    @Value("${calc-service-url}")
    private static String CALC_SERVICE_URL;

    public CalcService(PreferenceRepository preferenceRepository, DefinedGroupRepository definedGroupRepository, ResultRepository resultRepository, ProductRepository productRepository, EditionRepository editionRepository, EditionService editionService, RestTemplate restTemplate) {
        this.preferenceRepository = preferenceRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.resultRepository = resultRepository;
        this.productRepository = productRepository;
        this.editionRepository = editionRepository;
        this.editionService = editionService;
        this.restTemplate = restTemplate;
    }

    // todo test this
    public ApiResponse closeEdition(Long userId, Long editionId) {

        editionService.changeEditionStatus(userId, editionId, EditionStatusName.CLOSED);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String body = getMappedDataForEdition(editionId);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        String result = restTemplate.postForObject(CALC_SERVICE_URL + "/solve/", httpEntity, String.class);

        saveResultsFromJsonData(editionId, result);
        editionService.changeEditionStatus(userId, editionId, EditionStatusName.FINISHED);

        return new ApiResponse("Edition closed, you can now check for results");
    }

    private void saveResultsFromJsonData(Long editionId, String jsonString) {
        JSONArray jsonData = new JSONArray(jsonString);

        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject node = jsonData.getJSONObject(i);

            resultRepository.save(getResult(editionId, node));
        }
    }

    private Result getResult(Long editionId, JSONObject node) {
        Long receiversProductId = node.getLong("receiver");
        Long productToSentId = node.getLong("sender");

        Product productToSent = productRepository.findById(productToSentId).orElseThrow(() -> new AppException("Product not found, probably the calculation data have been corrupted"));
        Product receiversProduct = productRepository.findById(receiversProductId).orElseThrow(() -> new AppException("Product not found,probably the calculation data have been corrupted"));

        User receiver = receiversProduct.getUser();
        User sender = productToSent.getUser();

        Edition edition = editionRepository.findById(editionId).orElseThrow(() -> new AppException("Edition not found"));

        return new Result()
                .setEdition(edition)
                .setProductToSend(productToSent)
                .setReceiver(receiver)
                .setSender(sender);
    }

    private String getMappedDataForEdition(Long editionId) {
        Set<Preference> preferences = preferenceRepository.findAllByEdition_Id(editionId);
        Set<DefinedGroup> definedGroups = definedGroupRepository.findAllByEdition_Id(editionId);

        JSONObject mappedData = new JSONObject();
        JSONArray definedGroupsAsJsonTable = new JSONArray(definedGroups.stream().map(this::mapGroup).collect(Collectors.toList()));
        JSONArray preferencesAsJsonTable = new JSONArray(preferences.stream().map(this::mapPreference).collect(Collectors.toList()));

        return mappedData
                .put("named_groups", definedGroupsAsJsonTable)
                .put("preferences", preferencesAsJsonTable).toString();
    }

    private JSONObject mapGroup(DefinedGroup definedGroup) {
        return new JSONObject()
                .put("id", definedGroup.getId())
                .put("single_preferences", new JSONArray(definedGroup.getProducts().stream().map(Product::getId).collect(Collectors.toList())))
                .put("groups", new JSONArray(definedGroup.getGroups().stream().map(DefinedGroup::getId).collect(Collectors.toList())));
    }


    private JSONObject mapPreference(Preference preference) {
        return new JSONObject()
                .put("id", preference.getHaveProduct().getId())
                .put("single_preferences", new JSONArray(preference.getWantedProducts().stream().map(Product::getId).collect(Collectors.toList())))
                .put("groups", new JSONArray(preference.getWantedDefinedGroups().stream().map(DefinedGroup::getId).collect(Collectors.toList())));
    }
}
