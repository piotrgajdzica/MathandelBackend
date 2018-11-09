package mathandel.backend.utils;

public class UrlPaths {

    // todo tringi finale drukowanymi

    // /api/auth
    public static final String authPath = "/api/auth";
    public static final String signInPath = authPath + "/signIn";
    public static final String signUpPath = authPath + "/signUp";

    // /api/editions
    public static final String editionsPath = "/api/editions";
    // /api/editions/{editionId}
    public static final String editionPath = editionsPath + "/{editionId}";
    // /api/editions/{editionId}/participants
    public static final String editionParticipantsPath = editionPath + "/participants";
    // /api/editions/{editionId}/products
    public static final String editionProductsPath = editionPath + "/products";
    // /api/editions/{editionId}/products/{productId}
    public static final String editionProductPath = editionProductsPath + "/{productId}";
    // /api/editions/{editionId}/products/my
    public static final String editionMyProductsPath = editionProductsPath + "/my";
    // /api/editions/{editionId}/moderators
    public static final String editionModeratorsPath = editionPath + "/moderators";
    // /api/editions/{editonId}/preferences
    public static final String editionPreferencesPath = editionPath + "/{editonId}/preferences";
    // /api/editions/{editonId}/results
    public static final String editionResultsPath = editionPath + "/{editonId}/results";



    // /api/editions/{editionId}/defined-groups
    public static final String definedGroups = editionPath + "/defined-groups";
    // /api/editions/{editionId}/defined-groups/{groupId}
    public static final String definedGroup = definedGroups + "/{groupId}";
    // /api/editions/{editionId}/defined-groups/{groupId}/content
    public static final String definedGroupContent = definedGroup + "/content";

    // /api/products
    public static final String productsPath = "/api/products";
    // /api/products/{productId}
    public static final String productPath = productsPath + "/{productId}";
    // /api/products/{productId}/images
    public static final String productImagesPath = productPath + "/images";
    // /api/products/{productId}/images/{imageName}
    public static final String productImagePath = productImagesPath + "/{imageName}";
    // /api/products/not-assigned
    public static final String notAssignedProductsPath = productsPath + "/not-assigned";
    // /api/products/{productId}/preferences
    public static final String preferencesForProductPath =   productsPath + "/{productId}/preferences";


    // /api/users
    public static final String usersPath = "/api/users";
    // /api/users/{userId}
    public static final String userPath = usersPath + "/{userId}";
    // /api/users/me
    public static final String userMePath = usersPath + "/me";
    // /api/users/me/password
    public static final String userMePasswordPath = userMePath + "/password";

    // /api/images/{imageName}
    public static final String imagePath = "/images/{imageName}";

    // /api/moderatorRequests
    public static final String moderatorRequestsPath = "/api/moderator-requests";
    // /api/moderatorRequests
    public static final String moderatorRequestsResolvePath = moderatorRequestsPath + "/resolve";
    // /api/moderatorRequests
    public static final String moderatorRequestsGetMyRequests = moderatorRequestsPath + "/my";


    // /api/results/

    public static final String resultsRequestsPath = "/api/results/";

    public static final String userRatesRequestPath = "/api/results/{userId}";

    public static final String resultsProductsToSendByUserPath = "/api/results/{editionId}/send";

    public static final String resultsProductsToReceiveByUserPath = "/api/results/{editionId}/receive";

    // /api/finish/{editionId}

    public static final String closeEditionPath = "/api/finish/{editionId}";


}
