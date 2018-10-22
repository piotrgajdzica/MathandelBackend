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


    public static PreferenceTO mapPreference(Preference preference) {

        PreferenceTO preferenceTO = new PreferenceTO()
                .setHaveProductId(preference.getHaveProduct().getId())
                .setId(preference.getId())
                .setUserId(preference.getUser().getId());

        preferenceTO.getWantedProducts().addAll(preference.getWantedProducts().stream().map(e->e.getId()).collect(Collectors.toSet()));
        preferenceTO.getWantedDefinedGroups().addAll(preference.getWantedDefinedGroups().stream().map(e->e.getId()).collect(Collectors.toSet()));
        return preferenceTO;
    }

    private static Collection<DefinedGroupTO> mapDefinedGroups(Set<DefinedGroup> wantedDefinedGroups) {
        return wantedDefinedGroups.stream().map(ServerToClientDataConverter::mapDefinedGroup).collect(Collectors.toSet());
    }

    public static RoleTO mapRole(Role role) {
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

    public static EditionTO mapEdition(Edition edition) {
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


    public static UserTO mapUser(User user) {
        return new UserTO()
                .setId(user.getId())
                .setName(user.getName())
                .setSurname(user.getSurname())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setRoles(mapRoles(user.getRoles()));
    }

    public static Set<RateTO> mapRates(Set<Rate> rates) {
        return rates.stream().map(rate -> mapRate(rate)).collect(Collectors.toSet());
    }

    private static RateTO mapRate(Rate rate) {
        return new RateTO()
                .setId(rate.getId())
                .setComment(rate.getComment())
                .setRateName(rate.getRateName())
                .setResultId(rate.getResult().getId())
                .setRaterId(rate.getRater().getId());
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
