package mathandel.backend.service;

import mathandel.backend.client.model.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.Edition;
import mathandel.backend.model.EditionStatusType;
import mathandel.backend.model.Product;
import mathandel.backend.model.User;
import mathandel.backend.model.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ProductRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    private Long userId = 1L;
    private Long productId = 1L;
    private Long editionId = 1L;
    private String productName = "Name";
    private String productDescription = "Description";

    private User user = new User()
            .setId(userId);

    private ProductTO productTOrequest = new ProductTO()
            .setName(productName)
            .setDescription(productDescription);

    private ProductTO productTO = new ProductTO()
            .setUserId(userId)
            .setName(productName)
            .setDescription(productDescription)
            .setId(productId);

    private Product product = new Product()
            .setName(productName)
            .setDescription(productDescription)
            .setUser(user)
            .setId(productId);

    private Edition edition = new Edition()
            .setId(editionId)
            .setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.OPENED));

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    EditionRepository editionRepository;

    @Autowired
    ProductService productService;

    @Test
    public void shouldCreateProduct() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.save(any())).thenReturn(product);

        //when
        ProductTO actual = productService.createProduct(userId, productTOrequest);

        //then
        assertThat(actual).isEqualTo(productTO);
    }


    @Test
    public void shouldThrowExceptionOnCreatingProduct() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class,
                () -> productService.createProduct(userId, productTOrequest));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldEditProduct() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        ApiResponse apiResponse = productService.editProduct(userId, productTOrequest, productId);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Product edited successfully");
    }

    @Test
    public void shouldFailWhenProductsEditionIsClosed() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        product.setEdition(new Edition().setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED)));

        //when
        ApiResponse apiResponse = productService.editProduct(userId, productTOrequest, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Product's edition is not opened");
    }

    @Test
    public void shouldFailWhenUserHaveNoAccessToProduct() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        product.setUser(new User().setId(2L));

        //when
        ApiResponse apiResponse = productService.editProduct(userId, productTOrequest, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("You have no access to this resource");
    }

    @Test
    public void shouldFailWhenProductDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        ApiResponse apiResponse = productService.editProduct(userId, productTOrequest, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Product doesn't exist");
    }

    @Test
    public void shouldFailWhenUserDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        AppException appException = assertThrows(AppException.class, () -> productService.editProduct(userId, productTOrequest, productId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist.");
    }

    @Test
    public void shouldGetProduct() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        ProductTO actual = productService.getProduct(productId);

        //then
        assertThat(actual).isEqualTo(productTO);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnGetProduct() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(productId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Product");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(productId);
    }

    @Test
    public void shouldAssignProductToEdition() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Product successfully assigned to edition");
    }

    @Test
    public void shouldFailAssignProductInEdition() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product.setEdition(new Edition())));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Product already in edition");
    }

    @Test
    public void shouldFailAssignNoAccess() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product.setUser(new User().setId(10L))));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("You have no access to this product");
    }

    @Test
    public void shouldFailAssignEditionNotOpened() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition.setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED))));

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition isn't opened");
    }

    @Test
    public void shouldFailEditionDoesntExist() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Edition doesn't exist");
    }

    @Test
    public void shouldFailProductDoesntExist() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when
        ApiResponse apiResponse = productService.assignProductToEdition(userId, editionId, productId);

        //then
        assertThat(apiResponse.getSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Product doesn't exist");
    }

    @Test
    public void shouldGetNotAssignedProducts() {
        //given
        when(productRepository.findByUser_IdAndEditionIsNull(userId)).thenReturn(Collections.singleton(product));

        //when
        Set<ProductTO> productTOs = productService.getNotAssignedProducts(userId);

        //then
        assertThat(productTOs.size()).isEqualTo(1);
        assertThat(productTOs.contains(productTO)).isTrue();
    }

    @Test
    public void shouldGetProductsFromEdition() {
        //given
        when(editionRepository.existsById(editionId)).thenReturn(true);
        when(productRepository.findByEdition_IdAndUser_IdNot(editionId, userId)).thenReturn(Collections.singleton(product));

        //when
        Set<ProductTO> productTOs = productService.getProductsFromEdition(userId, editionId);

        //then
        assertThat(productTOs.size()).isEqualTo(1);
        assertThat(productTOs.contains(productTO)).isTrue();
    }

    @Test
    public void shouldThrowExceptionOnGetProductsFromEditionWhenEditionDoesntExist() {
        //given
        when(editionRepository.existsById(editionId)).thenReturn(false);

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductsFromEdition(userId, editionId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Edition");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(editionId);
    }

    @Test
    public void shouldGetMyProductsFromEdition() {
        //given
        when(editionRepository.existsById(editionId)).thenReturn(true);
        when(productRepository.findByEdition_IdAndUser_Id(editionId, userId)).thenReturn(Collections.singleton(product));

        //when
        Set<ProductTO> productTOs = productService.getMyProductsFromEdition(userId, editionId);

        //then
        assertThat(productTOs.size()).isEqualTo(1);
        assertThat(productTOs.contains(productTO)).isTrue();
    }

    @Test
    public void shouldThrowExceptionOnGetMyProductsFromEditionWhenEditionDoesntExist() {
        //given
        when(editionRepository.existsById(editionId)).thenReturn(false);

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> productService.getMyProductsFromEdition(userId, editionId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Edition");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(editionId);
    }

}