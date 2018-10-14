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
    public static RoleTO mapRole(Role role){
        return new RoleTO().setRoleName(role.getName());
    }

    public static Set<RoleTO> mapRoles(Set<Role> roles) {
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

    public static List<EditionTO> mapEditions(List<Edition> all) {
        return all.stream().map(ServerToClientDataConverter::mapEdition).collect(Collectors.toList());
    }

    public static EditionTO mapEdition(Edition edition){
        return new EditionTO()
                .setId(edition.getId())
                .setName(edition.getName())
                .setDescription(edition.getDescription())
                .setEndDate(edition.getEndDate())
                .setNumberOfParticipants(edition.getParticipants().size())
                .setMaxParticipants(edition.getMaxParticipants());
    }

    public static DefinedGroupTO mapDefinedGroup(DefinedGroup definedGroup) {
        return new DefinedGroupTO()
                .setId(definedGroup.getId())
                .setName(definedGroup.getName())
                .setNumberOfProducts(definedGroup.getProducts().size());
    }
}
