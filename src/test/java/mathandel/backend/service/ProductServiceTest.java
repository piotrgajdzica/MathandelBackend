package mathandel.backend.service;

import mathandel.backend.model.client.ProductTO;
import mathandel.backend.client.response.ApiResponse;
import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.Product;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
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

    private final Long userId = 1L;
    private final Long productId = 1L;
    private final Long editionId = 1L;
    private final String productName = "Name";
    private final String productDescription = "Description";

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
    public void shouldFailOnCreateProductUserDoesntExist() {
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
        when(productRepository.save(any())).thenReturn(productTO);


        //when
        ProductTO product = productService.editProduct(userId, productTOrequest, productId);

        //then
        assertThat(product.getName()).isEqualTo(productTO.getName());
        assertThat(product.getDescription()).isEqualTo(productTO.getDescription());
        assertThat(product.getUserId()).isEqualTo(productTO.getUserId());
    }

    @Test
    public void shouldFailOnEditProductEditionIsClosed() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        product.setEdition(new Edition().setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED)));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.editProduct(userId, productTOrequest, productId));
        assertThat(badRequestException.getMessage()).isEqualTo("Product's edition is not opened");
    }

    @Test
    public void shouldFailOnEditProductUserHaveNoAccessToProduct() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        product.setUser(new User().setId(2L));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.editProduct(userId, productTOrequest, productId));
        assertThat(badRequestException.getMessage()).isEqualTo("You have no access to this resource");
    }

    @Test
    public void shouldFailOnEditProductDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Product", "id", productId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> productService.editProduct(userId, productTOrequest, productId));
        assertThat(resourceNotFoundException).isEqualTo(expected);
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
        assertThat(apiResponse.getMessage()).isEqualTo("Product successfully assigned to edition");
    }

    @Test
    public void shouldFailOnAssignProductToEditionProductInEdition() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product.setEdition(new Edition())));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.assignProductToEdition(userId, editionId, productId));
        assertThat(badRequestException.getMessage()).isEqualTo("Product already in edition");
    }

    @Test
    public void shouldFailOnAssignProductToEditionNoAccess() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product.setUser(new User().setId(10L))));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.assignProductToEdition(userId, editionId, productId));
        assertThat(badRequestException.getMessage()).isEqualTo("You have no access to this product");
    }

    @Test
    public void shouldFailOnAssignProductToEditionNotOpened() {
        //given
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition.setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED))));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> productService.assignProductToEdition(userId, editionId, productId));
        assertThat(badRequestException.getMessage()).isEqualTo("Edition is not opened");
    }

    @Test
    public void shouldFailOnAssignProductToEditionEditionDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Edition", "id", editionId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class,
                () -> productService.assignProductToEdition(userId, editionId, productId));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFailOnAssignProductToEditionProductDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Product", "id", editionId);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class,
                () -> productService.assignProductToEdition(userId, editionId, productId));
        assertThat(actual).isEqualTo(expected);
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
    public void shouldFailOnGetProductsFromEditionWhenEditionDoesntExist() {
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