package mathandel.backend.service;

import mathandel.backend.model.client.EditionTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.Preference;
import mathandel.backend.model.server.Product;
import mathandel.backend.model.server.User;
import mathandel.backend.repository.*;
import org.junit.After;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.mockito.Mockito.clearInvocations;

//todo why is it ignored??
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class PreferenceServiceTest {

    private final Long wantProductId = 1L;
    private final Long wantGroupProductId = 1L;
    private final Long haveProductId = 2L;
    private final Long userId = 1L;

    private EditionTO editionTO = new EditionTO()
            .setName("Mathandel 4000")
            .setMaxParticipants(100)
            .setEndDate(LocalDate.now().plusMonths(2));

    private Long editionId = 1L;
    private Edition edition = new Edition();
    private Preference preference = new Preference();

    @MockBean
    UserRepository userRepository;


    @MockBean
    EditionStatusTypeRepository editionStatusTypeRepository;

    @MockBean
    EditionRepository editionRepository;

    @MockBean
    PreferenceRepository preferenceRepository;

    @MockBean
    DefinedGroupRepository definedGroupRepository;

    @Autowired
    PreferenceService preferenceService;

    @Mock
    private User user;

    @Mock
    private Product haveProduct;
    @Mock
    private Product wantProduct;


    @After
    public void tearDown() {
        clearInvocations(haveProduct, wantProduct, user, userRepository, editionStatusTypeRepository, editionRepository, preferenceRepository, definedGroupRepository);
    }
    // todo to refactor :'(


//    @MockBean
//    ProductRepository productRepository;
//    private DefinedGroup wantDefinedGroup = new DefinedGroup();
//
//    @Test
//    public void shouldAddNewPreference() {
//        // given
//        when(productRepository.findById(haveProductId)).thenReturn(Optional.of(haveProduct));
//        when(haveProduct.getUser()).thenReturn(user);
//        when(user.getId()).thenReturn(userId);
//        when(preferenceRepository.findByHaveProduct_Id(haveProductId)).thenReturn(null);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
//        Set<Long> wantProductsIds = new HashSet<>();
//        wantProductsIds.add(wantProductId);
//        when(productRepository.findById(wantProductId)).thenReturn(Optional.of(wantProduct));
//
//        // when
//        ApiResponse apiResponse = preferenceService.updatePreference(userId, haveProductId, wantProductsIds, editionId);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Preference saved");
//
//    }
//
//    @Test
//    public void shouldAddToExistingPreference() {
//        // given
//        when(productRepository.findById(haveProductId)).thenReturn(Optional.of(haveProduct));
//        when(haveProduct.getUser()).thenReturn(user);
//        when(user.getId()).thenReturn(userId);
//        when(preferenceRepository.findByHaveProduct_Id(haveProductId)).thenReturn(preference);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
//        Set<Long> wantProductsIds = new HashSet<>();
//        wantProductsIds.add(wantProductId);
//        when(productRepository.findById(wantProductId)).thenReturn(Optional.of(wantProduct));
//
//        // when
//        ApiResponse apiResponse = preferenceService.updatePreference(userId, haveProductId, wantProductsIds, editionId);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Preference saved");
//
//    }
//
//    @Test
//    public void shouldAddNewGroupPreference() {
//        // given
//        when(productRepository.findById(haveProductId)).thenReturn(Optional.of(haveProduct));
//        when(haveProduct.getUser()).thenReturn(user);
//        when(user.getId()).thenReturn(userId);
//        when(preferenceRepository.findByHaveProduct_Id(haveProductId)).thenReturn(null);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
//        Set<Long> wantGroupProductsIds = new HashSet<>();
//        wantGroupProductsIds.add(wantGroupProductId);
//        when(definedGroupRepository.findById(wantGroupProductId)).thenReturn(Optional.of(wantDefinedGroup));
//
//        // when
//        ApiResponse apiResponse = preferenceService.updatePreference(userId, haveProductId, wantGroupProductsIds, editionId);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Preference saved");
//
//    }
//
//
//    @Test
//    public void shouldAddToExistingGroupPreference() {
//        // given
//        when(productRepository.findById(haveProductId)).thenReturn(Optional.of(haveProduct));
//        when(haveProduct.getUser()).thenReturn(user);
//        when(user.getId()).thenReturn(userId);
//        when(preferenceRepository.findByHaveProduct_Id(haveProductId)).thenReturn(preference);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
//        Set<Long> wantGroupProductsIds = new HashSet<>();
//        wantGroupProductsIds.add(wantGroupProductId);
//        when(definedGroupRepository.findById(wantGroupProductId)).thenReturn(Optional.of(wantDefinedGroup));
//
//        // when
//        ApiResponse apiResponse = preferenceService.updatePreference(userId, haveProductId, wantGroupProductsIds, editionId);
//
//        // then
//        assertEquals(apiResponse.getMessage(), "Preference saved");
//
//    }

    // getUserPreferencesFromOneEdition nie ma testu bo to jest zwykle wywolanie metody z repository


}