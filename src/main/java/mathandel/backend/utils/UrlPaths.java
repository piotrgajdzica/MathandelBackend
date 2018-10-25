package mathandel.backend.utils;

public class UrlPaths {

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

}
