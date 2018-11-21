package mathandel.backend.service;

import mathandel.backend.exception.AppException;
import mathandel.backend.exception.BadRequestException;
import mathandel.backend.exception.ResourceNotFoundException;
import mathandel.backend.model.client.ItemTO;
import mathandel.backend.model.server.Edition;
import mathandel.backend.model.server.EditionStatusType;
import mathandel.backend.model.server.Item;
import mathandel.backend.model.server.User;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.repository.EditionRepository;
import mathandel.backend.repository.ItemRepository;
import mathandel.backend.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTest {

    private final Long userId = 1L;
    private final Long itemId = 1L;
    private final Long editionId = 1L;
    private final String itemName = "Name";
    private final String itemDescription = "Description";

    private User user = new User()
            .setId(userId);

    private ItemTO itemTOrequest = new ItemTO()
            .setName(itemName)
            .setDescription(itemDescription);

    private ItemTO itemTO = new ItemTO()
            .setUserId(userId)
            .setName(itemName)
            .setDescription(itemDescription)
            .setId(itemId)
            .setImages(new HashSet<>());

    private Item item = new Item()
            .setName(itemName)
            .setDescription(itemDescription)
            .setUser(user)
            .setId(itemId);

    private Edition edition = new Edition()
            .setId(editionId)
            .setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.OPENED))
            .setParticipants(Collections.singleton(user));

    @MockBean
    UserRepository userRepository;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    EditionRepository editionRepository;

    @Autowired
    ItemService itemService;

    @Test
    public void shouldCreateItem() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when
        ItemTO actual = itemService.createItem(userId, editionId, itemTOrequest);

        //then
        assertThat(actual).isEqualTo(itemTO);
    }


    @Test
    public void shouldFailOnCreateItemUserDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        AppException appException = assertThrows(AppException.class,
                () -> itemService.createItem(userId, editionId, itemTOrequest));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist");
    }

    @Test
    public void shouldEditItem() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);


        //when
        ItemTO item = itemService.editItem(userId, itemTOrequest, itemId);

        //then
        assertThat(item.getName()).isEqualTo(itemTO.getName());
        assertThat(item.getDescription()).isEqualTo(itemTO.getDescription());
        assertThat(item.getUserId()).isEqualTo(itemTO.getUserId());
    }

    @Test
    public void shouldFailOnEditItemEditionIsClosed() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        item.setEdition(new Edition().setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED)));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.editItem(userId, itemTOrequest, itemId));
        assertThat(badRequestException.getMessage()).isEqualTo("Item's edition is not opened");
    }

    @Test
    public void shouldFailOnEditItemUserHaveNoAccessToItem() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        item.setUser(new User().setId(2L));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.editItem(userId, itemTOrequest, itemId));
        assertThat(badRequestException.getMessage()).isEqualTo("You have no access to this resource");
    }

    @Test
    public void shouldFailOnEditItemDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Item", "id", itemId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> itemService.editItem(userId, itemTOrequest, itemId));
        assertThat(resourceNotFoundException).isEqualTo(expected);
    }

    @Test
    public void shouldFailWhenUserDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when
        AppException appException = assertThrows(AppException.class, () -> itemService.editItem(userId, itemTOrequest, itemId));
        assertThat(appException.getMessage()).isEqualTo("User doesn't exist.");
    }

    @Test
    public void shouldGetItem() {
        //given
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        //when
        ItemTO actual = itemService.getItem(itemId);

        //then
        assertThat(actual).isEqualTo(itemTO);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionOnGetItem() {
        //given
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () -> itemService.getItem(itemId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Item");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(itemId);
    }

    //todo fix
//    @Test
//    public void shouldAssignItemToEdition() {
//        //given
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
//        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
//
//        //when
//        ApiResponse apiResponse = itemService.assignItemToEdition(userId, editionId, itemId);
//
//        //then
//        assertThat(apiResponse.getMessage()).isEqualTo("Item successfully assigned to edition");
//    }

    @Test
    public void shouldFailOnAssignItemToEditionItemInEdition() {
        //given
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item.setEdition(new Edition())));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.assignItemToEdition(userId, editionId, itemId));
        assertThat(badRequestException.getMessage()).isEqualTo("Item already in edition");
    }

    @Test
    public void shouldFailOnAssignItemToEditionNoAccess() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item.setUser(new User().setId(10L))));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.assignItemToEdition(userId, editionId, itemId));
        assertThat(badRequestException.getMessage()).isEqualTo("You have no access to this item");
    }

    @Test
    public void shouldFailOnAssignItemToEditionNotOpened() {
        //given
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition.setEditionStatusType(new EditionStatusType().setEditionStatusName(EditionStatusName.CLOSED))));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> itemService.assignItemToEdition(userId, editionId, itemId));
        assertThat(badRequestException.getMessage()).isEqualTo("Edition is not opened");
    }

    @Test
    public void shouldFailOnAssignItemToEditionEditionDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Edition", "id", editionId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(editionRepository.findById(editionId)).thenReturn(Optional.empty());

        //when then
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class,
                () -> itemService.assignItemToEdition(userId, editionId, itemId));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFailOnAssignItemToEditionItemDoesntExist() {
        //given
        ResourceNotFoundException expected = new ResourceNotFoundException("Item", "id", editionId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when then
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class,
                () -> itemService.assignItemToEdition(userId, editionId, itemId));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGetNotAssignedItems() {
        //given
        when(itemRepository.findByUser_IdAndEditionIsNull(userId)).thenReturn(Collections.singleton(item));

        //when
        Set<ItemTO> itemTOS = itemService.getNotAssignedItems(userId);

        //then
        assertThat(itemTOS.size()).isEqualTo(1);
        assertThat(itemTOS.contains(itemTO)).isTrue();
    }

    @Test
    public void shouldGetItemsFromEdition() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(editionRepository.existsById(editionId)).thenReturn(true);
        when(itemRepository.findByEdition_IdAndUser_IdNot(editionId, userId)).thenReturn(Collections.singleton(item));

        //when
        Set<ItemTO> itemTOS = itemService.getItemsFromEdition(userId, editionId);

        //then
        assertThat(itemTOS.size()).isEqualTo(1);
        assertThat(itemTOS.contains(itemTO)).isTrue();
    }

    @Test
    public void shouldFailOnGetItemsFromEditionWhenEditionDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(editionRepository.existsById(editionId)).thenReturn(false);

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> itemService.getItemsFromEdition(userId, editionId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Edition");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(editionId);
    }

    @Test
    public void shouldGetMyItemsFromEdition() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(editionRepository.findById(editionId)).thenReturn(Optional.of(edition));
        when(editionRepository.existsById(editionId)).thenReturn(true);
        when(itemRepository.findByEdition_IdAndUser_Id(editionId, userId)).thenReturn(Collections.singleton(item));

        //when
        Set<ItemTO> itemTOS = itemService.getMyItemsFromEdition(userId, editionId);

        //then
        assertThat(itemTOS.size()).isEqualTo(1);
        assertThat(itemTOS.contains(itemTO)).isTrue();
    }

    @Test
    public void shouldThrowExceptionOnGetMyItemsFromEditionWhenEditionDoesntExist() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(editionRepository.existsById(editionId)).thenReturn(false);

        //when then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> itemService.getMyItemsFromEdition(userId, editionId));
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Edition");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("id");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo(editionId);
    }

}