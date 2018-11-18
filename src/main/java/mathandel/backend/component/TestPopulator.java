package mathandel.backend.component;

import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.client.PreferenceTO;
import mathandel.backend.model.client.ProductTO;
import mathandel.backend.model.client.request.SignUpRequest;
import mathandel.backend.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

/*
Readme

this is just a component for testing

if you want to use it add @Transactional annotations in methods
    - UserService.joinEdition()
    - PreferenceService.updatePreference()
    - EditionService.createEdition()
    - ProductService.createProduct()
    - CalcService.closeEdition()

otherwise lazy fetch error will occur

 */
@Component
public class TestPopulator {

    private final AuthService authService;
    private final EditionService editionService;
    private final ProductService productService;
    private final PreferenceService preferenceService;
    private final CalcService calcService;
    private final UserService userService;

    public TestPopulator(AuthService authService, EditionService editionService, ProductService productService, PreferenceService preferenceService, CalcService calcService, UserService userService) {
        this.authService = authService;
        this.editionService = editionService;
        this.productService = productService;
        this.preferenceService = preferenceService;
        this.calcService = calcService;
        this.userService = userService;
    }

    public void populate() {
        createEdition();
        createUsers();
        createProducts();
        createPreferences();
        closeEdition();
    }

    private void createEdition() {
        EditionTO editionTO = new EditionTO()
                .setName("Mathandel")
                .setMaxParticipants(10)
                .setDescription("Description")
                .setEndDate(LocalDate.now().plusMonths(10));

        editionService.createEdition(editionTO, 1L);
    }

    private void createUsers() {
        SignUpRequest signUpRequest1 = new SignUpRequest()
                .setUsername("user1")
                .setEmail("user1user1@user1.com")
                .setPassword("user1user1")
                .setName("user1")
                .setSurname("user1")
                .setAddress("user1")
                .setCity("user1")
                .setCountry("user1")
                .setPostalCode("user1");

        SignUpRequest signUpRequest2 = new SignUpRequest()
                .setUsername("user2")
                .setEmail("user2user2@user2.com")
                .setPassword("user2user2")
                .setName("user2")
                .setSurname("user2")
                .setAddress("user2")
                .setCity("user2")
                .setCountry("user2")
                .setPostalCode("user2");

        authService.signUp(signUpRequest1);
        userService.joinEdition(2L, 1L);
        authService.signUp(signUpRequest2);
        userService.joinEdition(3L, 1L);
    }

    private void createProducts() {
        ProductTO product1 = new ProductTO()
                .setName("product1")
                .setDescription("product1 description")
                .setImages(Collections.emptySet());

        ProductTO product2 = new ProductTO()
                .setName("product2")
                .setDescription("product2 description")
                .setImages(Collections.emptySet());

        ProductTO product3 = new ProductTO()
                .setName("product3")
                .setDescription("product3 description")
                .setImages(Collections.emptySet());

        productService.createProduct(1L, 1L, product1);
        productService.createProduct(2L, 1L, product2);
        productService.createProduct(3L, 1L, product3);
    }

    private void createPreferences() {
        PreferenceTO preference1 = new PreferenceTO()
                .setWantedProductsIds(Collections.singleton(2L))
                .setWantedDefinedGroupsIds(Collections.emptySet());
        PreferenceTO preference2 = new PreferenceTO()
                .setWantedProductsIds(Collections.singleton(3L))
                .setWantedDefinedGroupsIds(Collections.emptySet());
        PreferenceTO preference3 = new PreferenceTO()
                .setWantedProductsIds(Collections.singleton(1L))
                .setWantedDefinedGroupsIds(Collections.emptySet());

        preferenceService.updatePreference(1L, preference1, 1L, 1L);
        preferenceService.updatePreference(2L, preference2, 1L, 2L);
        preferenceService.updatePreference(3L, preference3, 1L, 3L);
    }

    private void closeEdition() {
        calcService.closeEdition(1L, 1L);
    }
}
