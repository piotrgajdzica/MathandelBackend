package mathandel.backend.utils;

import mathandel.backend.model.client.*;
import mathandel.backend.model.server.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerToClientDataConverter {
    public static ModeratorRequestTO mapModeratorRequest(ModeratorRequest moderatorRequest) {
                return new ModeratorRequestTO()
                        .setModeratorRequestStatus(moderatorRequest.getModeratorRequestStatus().getName())
                        .setReason(moderatorRequest.getReason())
                        .setUser(mapUserFull(moderatorRequest.getUser()));
    }

    private static UserTO mapUserFull(User user) {
        // todo merge
        return null;
    }

    public static PreferenceTO mapPreference(Preference preference) {
        return new PreferenceTO()
                .setDefinedGroupId(mapDefinedGroup(preference.getDefinedGroup()))
                .setHaveProductId(mapProduct(preference.getHaveProduct()))
                .setWantProductId(mapProduct(preference.getWantProduct()))
                .setId(preference.getId())
                .setUserId(preference.getUser().getId());
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
                .setUserId(product.getUser().getId());

    }

    public static Set<ProductTO> mapProducts(Set<Product> products) {
        return products.stream().map(ServerToClientDataConverter::mapProduct).collect(Collectors.toSet());
    }

    public static DefinedGroupTO mapDefinedGroup(DefinedGroup definedGroup) {
        return new DefinedGroupTO()
                .setId(definedGroup.getId())
                .setName(definedGroup.getName())
                .setNumberOfProducts(definedGroup.getProducts().size());
    }

    public static DefinedGroupContentTO mapGroupContent(Set<Product> products, Set<DefinedGroup> groups) {

        Set<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toSet());
        Set<Long> groupIds = groups.stream().map(DefinedGroup::getId).collect(Collectors.toSet());

        return new DefinedGroupContentTO()
                .setProductsIds(productIds)
                .setGroupIds(groupIds);
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
}
