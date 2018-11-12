package mathandel.backend.utils;

import mathandel.backend.model.client.*;
import mathandel.backend.model.server.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerToClientDataConverter {
    public static ModeratorRequestTO mapModeratorRequest(ModeratorRequest moderatorRequest) {
        return new ModeratorRequestTO()
                .setModeratorRequestStatus(moderatorRequest.getModeratorRequestStatus().getName())
                .setReason(moderatorRequest.getReason())
                .setUserId(moderatorRequest.getUser().getId())
                .setId(moderatorRequest.getId());
    }

    // todo implement equals and hashcode everywhere
    public static Set<ModeratorRequestTO> mapModeratorRequests(Set<ModeratorRequest> moderatorRequests){
        return moderatorRequests.stream().map(ServerToClientDataConverter::mapModeratorRequest).collect(Collectors.toSet());
    }

    public static PreferenceTO mapPreference(Preference preference) {
        return new PreferenceTO()
                .setId(preference.getId())
                .setUserId(preference.getUser().getId())
                .setHaveProductId(preference.getHaveProduct().getId())
                .setWantedProductsIds(preference.getWantedProducts().stream().map(Product::getId).collect(Collectors.toSet()))
                .setWantedDefinedGroupsIds(preference.getWantedDefinedGroups().stream().map(DefinedGroup::getId).collect(Collectors.toSet()));
    }

    public static Set<PreferenceTO> mapPreferences(Set<Preference> preferences) {
        return preferences.stream().map(ServerToClientDataConverter::mapPreference).collect(Collectors.toSet());
    }

    public static String mapRole(Role role){
        return role.getName().toString();
    }

    public static Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(ServerToClientDataConverter::mapRole).collect(Collectors.toSet());
    }

    public static ProductTO mapProduct(Product product) {
        return new ProductTO()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setUserId(product.getUser().getId())
                .setEditionId(product.getEdition() != null ? product.getEdition().getId() : null)
                .setImages(mapImages(product.getImages()));
    }

    public static Set<ProductTO> mapProducts(Set<Product> products) {
        return products.stream().map(ServerToClientDataConverter::mapProduct).collect(Collectors.toSet());
    }

    public static DefinedGroupTO mapDefinedGroup(DefinedGroup definedGroup) {
        return new DefinedGroupTO()
                .setId(definedGroup.getId())
                .setName(definedGroup.getName())
                .setProductsIds(definedGroup.getProducts().stream().map(Product::getId).collect(Collectors.toSet()))
                .setGroupIds(definedGroup.getGroups().stream().map(DefinedGroup::getId).collect(Collectors.toSet()));
    }

    public static Set<DefinedGroupTO> mapDefinedGroups(Set<DefinedGroup> groups) {
        return groups.stream().map(ServerToClientDataConverter::mapDefinedGroup).collect(Collectors.toSet());
    }

    public static List<EditionTO> mapEditions(List<Edition> all, Long userId) {
        return all.stream().map(edition -> mapEdition(edition, userId)).collect(Collectors.toList());
    }

    public static EditionTO mapEdition(Edition edition, Long userId) {
        return new EditionTO()
                .setId(edition.getId())
                .setName(edition.getName())
                .setDescription(edition.getDescription())
                .setEndDate(edition.getEndDate())
                .setNumberOfParticipants(edition.getParticipants().size())
                .setMaxParticipants(edition.getMaxParticipants())
                .setModerator(edition.getModerators().stream().anyMatch(participant -> participant.getId().equals(userId)))
                .setParticipant(edition.getParticipants().stream().anyMatch(participant -> participant.getId().equals(userId)));
    }

    private static Set<ImageTO> mapImages(Set<Image> images) {
        return images.stream().map(ServerToClientDataConverter::mapImage).collect(Collectors.toSet());
    }

    public static ImageTO mapImage(Image image) {
        return new ImageTO()
                .setId(image.getId())
                .setName(image.getName());
    }

    public static UserTO mapUser(User user) {
        return new UserTO()
                .setId(user.getId())
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRoles(mapRoles(user.getRoles()))
                .setAddress(user.getAddress())
                .setCity(user.getCity())
                .setPostalCode(user.getPostalCode())
                .setCountry(user.getCountry());
    }

    public static Set<TransactionRateTO> mapRates(Set<TransactionRate> transactionRates) {
        return transactionRates.stream().map(transactionRate -> mapRate(transactionRate)).collect(Collectors.toSet());
    }

    private static TransactionRateTO mapRate(TransactionRate transactionRate) {
        return new TransactionRateTO()
                .setId(transactionRate.getId())
                .setComment(transactionRate.getComment())
                .setRateName(transactionRate.getRate().getName())
                .setResultId(transactionRate.getResult().getId())
                .setRaterId(transactionRate.getRater().getId());
    }

    private static ResultTO mapResult(Result result){
        return new ResultTO()
                .setId(result.getId())
                .setReceiverId(result.getReceiver().getId())
                .setSenderId(result.getReceiver().getId())
                .setEditionId(result.getEdition().getId());

    }

    public static Set<ResultTO> mapResults(Set<Result> results){
        return results.stream().map(ServerToClientDataConverter::mapResult).collect(Collectors.toSet());
    }
}
