package mathandel.backend.component;

import mathandel.backend.exception.AppException;
import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class FullDBPopulator {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EditionStatusTypeRepository editionStatusTypeRepository;
    private final EditionRepository editionRepository;
    private final DefinedGroupRepository definedGroupRepository;
    private final ItemRepository itemRepository;
    private final PreferenceRepository preferenceRepository;

    private int editionNumb = 5;
    private int usersNumb = 10;
    private int itemNumb = 30;
    private int definedGroupsNumb = 20;
    private List<Edition> editions = new LinkedList<>();
    private List<User> users = new LinkedList<>();
    private List<Item> items = new LinkedList<>();
    private List<DefinedGroup> definedGroups = new LinkedList<>();
    private List<Preference> preferences = new LinkedList<>();
    private Random random;

    public FullDBPopulator(RoleRepository roleRepository, UserRepository userRepository, EditionStatusTypeRepository editionStatusTypeRepository, RateTypeRepository rateTypeRepository, EditionRepository editionRepository, DefinedGroupRepository definedGroupRepository, ItemRepository itemRepository, ResultRepository resultRepository, RateRepository rateRepository, PreferenceRepository preferenceRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
        this.editionRepository = editionRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.itemRepository = itemRepository;
        this.preferenceRepository = preferenceRepository;
        random = new Random();
    }

    public void populate() {
        createUsers();
        createEditions();
        assignRandomlyUsersToEditions();
        createItems();
        createDefinedGroups();
        createPreferences();

        save();
    }

    private void save() {
        userRepository.saveAll(users);
        Thread.yield();
        editionRepository.saveAll(editions);
        Thread.yield();
        itemRepository.saveAll(items);
        Thread.yield();
        definedGroupRepository.saveAll(definedGroups);
        Thread.yield();
        preferenceRepository.saveAll(preferences);
    }

    private void createUsers() {
        for (int i = 0; i < usersNumb; i++) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("Roles not initialized correctly")));
            User user = new User()
                    .setEmail(randMail())
                    .setName(randName(i))
                    .setAddress(randName(i))
                    .setCity(randName(i))
                    .setCountry(randName(i))
                    .setPostalCode(randName(i))
                    .setPassword(randPasswd(i))
                    .setSurname(randSurname(i))
                    .setUsername(randUsername(i))
                    .setRoles(roles);
            users.add(user);
        }
    }

    private void createEditions() {
        for (int i = 0; i < editionNumb; i++) {
            Edition edition = new Edition()
                    .setEditionStatusType(createEditionStatusType())
                    .setEndDate(LocalDate.now())
                    .setMaxParticipants(random.nextInt(10) + 10)
                    .setDescription(" Edition " + i)
                    .setModerators(new HashSet<>())
                    .setParticipants(new HashSet<>())
                    .setName(randEditionName(i));
            editions.add(edition);
        }
    }

    private String randEditionName(int i) {
        return "editionname" + i;
    }

    private void assignRandomlyUsersToEditions() {

        for (int i = 0; i < editionNumb; i++) {
            Set<User> participants = editions.get(i).getParticipants();
            Set<User> moderators = editions.get(i).getModerators();
            for (int j = 0; j < usersNumb; j++) {
                if (random.nextInt(2) == 1) {
                    participants.add(users.get(j));
                    if (random.nextInt(5) == 1) {
                        User user = users.get(j);
                        user.getRoles().add((roleRepository.findByName(RoleName.ROLE_MODERATOR).orElseThrow(() -> new AppException("Roles not initialized correctly"))));
                        moderators.add(user);
                    }
                }
            }
            if (moderators.isEmpty()) {
                moderators.add(users.get(random.nextInt(usersNumb)));
                participants.add(users.get(random.nextInt(usersNumb)));
            }
        }
    }

    private void createItems() {
        for (int i = 0; i < itemNumb; i++) {
            Edition edition = getRandEdition();

            while (edition.getParticipants().isEmpty()) {
                edition = getRandEdition();
            }

            User user = edition.getParticipants().iterator().next();
            Item item = new Item()
                    .setName(randName(i))
                    .setDescription(randName(i))
                    .setEdition(edition)
                    .setUser(user);
            items.add(item);
        }
    }

    private void createDefinedGroups() {
        int curItemsNumb = itemNumb;
        for (int i = 0; i < definedGroupsNumb; i++) {
            Edition edition = getRandEdition();

            while (edition.getParticipants().isEmpty()) {
                edition = getRandEdition();
            }

            User user = getRandUserFromEdition(edition);
            if (user == null) {
                continue;
            }

            int copy = curItemsNumb;

            DefinedGroup definedGroup = new DefinedGroup()
                    .setEdition(edition)
                    .setName(randName(i))
                    .setItems(new HashSet<>())
                    .setGroups(new HashSet<>())
                    .setUser(user);

            for (int j = curItemsNumb; j < copy + 3; j++) {
                Item item = new Item()
                        .setName(randName(j))
                        .setDescription(randName(j))
                        .setEdition(edition)
                        .setUser(user);
                items.add(item);
                definedGroup.getItems().add(item);
                curItemsNumb++;
            }
            curItemsNumb = copy;
            definedGroups.add(definedGroup);
        }
        itemNumb += (definedGroupsNumb * 3);

        for (int j = 0; j < definedGroups.size() - 1; j++) {
            if (definedGroupBelongToTheSameEdition(definedGroups.get(j), definedGroups.get(j + 1))
                    && definedGroupsBelongToTheSameUser(definedGroups.get(j), definedGroups.get(j + 1))
                    && random.nextInt(3) == 1) {

                definedGroups.get(j).getGroups().add(definedGroups.get(j + 1));
            }
        }
    }

    private User getRandUserFromEdition(Edition edition) {
        Set<User> participants = edition.getParticipants();
        User ret = null;
        Iterator<User> iterator = participants.iterator();

        while (iterator.hasNext()) {
            ret = iterator.next();
            if (random.nextInt(3) == 1) break;
        }
        return ret;
    }

    private void createPreferences() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < items.size() - 1; j++) {
                if (areNeightbourItemsInTheSameEdition(items.get(j), items.get(j + 1))
                        && !itemsBelongsToTheSameUser(items.get(j), items.get(j + 1))
                        && random.nextInt(2) == 1) {
                    preferences.add(createPreference(items.get(j), items.get(j + 1)));
                }
            }
            Collections.shuffle(items);
        }


        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = 0; j < definedGroups.size() - 1; j++) {
                if (definedGroupAndItemBelongToTheSameEdition(items.get(i), definedGroups.get(j))
                        && !definedGroupsAndItemBelongToTheSameUser(items.get(i), definedGroups.get(j))
                        && random.nextInt(2) == 1) {

                    if (preferencesOfItem.containsKey(items.get(i).getName())) {
                        preferencesOfItem.get(items.get(i).getName()).getWantedDefinedGroups().add(definedGroups.get(j));
                    }
                }
            }
        }
    }

    private boolean definedGroupsAndItemBelongToTheSameUser(Item item, DefinedGroup definedGroup) {
        return item.getUser().getEmail().equals(definedGroup.getUser().getEmail());
    }

    private boolean definedGroupAndItemBelongToTheSameEdition(Item item, DefinedGroup definedGroup) {
        return item.getEdition().getName().equals(definedGroup.getEdition().getName());
    }

    private boolean definedGroupsBelongToTheSameUser(DefinedGroup definedGroup, DefinedGroup definedGroup1) {
        return definedGroup.getUser().getEmail().equals(definedGroup1.getUser().getEmail());
    }

    private boolean definedGroupBelongToTheSameEdition(DefinedGroup definedGroup, DefinedGroup definedGroup1) {
        return definedGroup.getEdition().getName().equals(definedGroup1.getEdition().getName());
    }

    private boolean itemsBelongsToTheSameUser(Item item, Item item1) {
        return item.getUser().getEmail().equals(item1.getUser().getEmail());
    }

    private Map<String, Preference> preferencesOfItem = new HashMap<>();

    private Preference createPreference(Item item, Item item1) {

        Preference preference = preferencesOfItem.get(item.getName());
        if (preference == null) {
            preference = new Preference()
                    .setUser(item.getUser())
                    .setEdition(item.getEdition())
                    .setHaveItem(item)
                    .setWantedItems(new HashSet<>())
                    .setWantedDefinedGroups(new HashSet<>())
                    .setEdition(item.getEdition());
            preferencesOfItem.put(item.getName(), preference);
        }
        preference.getWantedItems().add(item1);

        return preference;
    }

    private boolean areNeightbourItemsInTheSameEdition(Item a, Item b) {
        return a.getEdition().getName().equals(b.getEdition().getName());
    }

    private Edition getRandEdition() {
        int i = random.nextInt(editionNumb);
        return editions.get(i);
    }

    private String randUsername(int i) {
        return "userName" + i;
    }

    private String randSurname(int i) {
        return "surname" + i;
    }

    private String randPasswd(int i) {
        return "pass" + i;
    }

    private String randName(int i) {
        return "name" + i;
    }

    private String randMail() {
        return "mail" + random.nextInt() + "@gmail.com";
    }

    private EditionStatusType createEditionStatusType() {
        return editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.OPENED);
    }
}
