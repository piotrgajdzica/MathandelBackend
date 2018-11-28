package mathandel.backend.component;

import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.EditionStatusName;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MathandelDataPopulator {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private DefinedGroupRepository definedGroupRepository;
    private PreferenceRepository preferenceRepository;
    private RoleRepository roleRepository;
    private EditionRepository editionRepository;
    private EditionStatusTypeRepository editionStatusTypeRepository;

    private Set<String> userNames = new HashSet<>();
    private Set<ItemData> items = new HashSet<>();
    private Set<DefinedGroupData> definedGroups = new HashSet<>();
    private Set<PreferenceLocal> preferences = new HashSet<>();

    private long maxId = 0;
    private Edition edition;

    private long lStartTime;
    private long lEndTime;
    private long output;

    public MathandelDataPopulator(PasswordEncoder passwordEncoder, UserRepository userRepository, ItemRepository itemRepository, DefinedGroupRepository definedGroupRepository, PreferenceRepository preferenceRepository, RoleRepository roleRepository, EditionRepository editionRepository, EditionStatusTypeRepository editionStatusTypeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.preferenceRepository = preferenceRepository;
        this.roleRepository = roleRepository;
        this.editionRepository = editionRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
    }

    public void saveFromFile(String fileName) throws IOException {

        lStartTime = System.nanoTime();
        initEdition();
        lEndTime = System.nanoTime();
        output = lEndTime - lStartTime;
        System.out.println("Created edition -- " + output / 1000000000);

        lStartTime = System.nanoTime();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        while (line != null && !line.equals("\u001A")) {

            if (line.startsWith("#") || line.isEmpty() || line.startsWith("\n")) {
                line = bufferedReader.readLine();
                continue;
            }

            String[] splitLine = line.split(":");
            String[] leftSideOfCollon = splitLine[0].trim().split("\\)");

            Set<String> rightSideOfCollon = new HashSet<>();
            if (splitLine.length > 1) {
                rightSideOfCollon = Arrays.stream(splitLine[1].trim().split(";")).flatMap(e -> Arrays.stream(e.split(" "))).filter(a -> !a.isEmpty()).collect(Collectors.toSet());
            }

            switch (leftSideOfCollon[1].trim().charAt(0)) {
                case '%':
                    parseDefinedGroupDefinition(leftSideOfCollon, rightSideOfCollon);
                    break;

                default:
                    parsePreference(leftSideOfCollon, rightSideOfCollon);
                    break;
            }
            line = bufferedReader.readLine();
        }
        lEndTime = System.nanoTime();
        System.out.println("Parser  -- " + output / 1000000000);

        saveDataToDb();

    }

    private void initEdition() {
        // todo init edition
        EditionStatusType editionStatusType = editionStatusTypeRepository.findByEditionStatusName(EditionStatusName.OPENED);
        Set<User> mods = new HashSet<>();
        mods.add(userRepository.findById(1L).get());
        edition = new Edition()
                .setName("Mathandel test")
                .setDescription("desc")
                .setMaxParticipants(Integer.MAX_VALUE)
                .setModerators(mods)
                .setEditionStatusType(editionStatusType)
                .setEndDate(LocalDate.now().plusMonths(10));

        editionRepository.save(edition);
    }

    private void saveDataToDb() {
        lStartTime = System.nanoTime();
        saveUsers();
        lEndTime = System.nanoTime();
        output = lEndTime - lStartTime;
        System.out.println("Saved users -- " + output / 1000000000);
        lStartTime = System.nanoTime();
        saveItems();
        lEndTime = System.nanoTime();
        output = lEndTime - lStartTime;
        System.out.println("Saved items -- " + output / 1000000000);
        lStartTime = System.nanoTime();
        saveItemsNotPresent();
        lEndTime = System.nanoTime();
        output = lEndTime - lStartTime;
        System.out.println("Saved not present items -- " + output / 1000000000);

        saveDefinedGroups();

        lStartTime = System.nanoTime();
        savePreferences();
        lEndTime = System.nanoTime();
        output = lEndTime - lStartTime;
        System.out.println("Saved preferences -- " + output / 1000000000);
    }

    private void saveUsers() {
        Set<User> users = new HashSet<>();
        int i = 0;
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).get());

        for (String userName : userNames) {
            User user = new User()
                    .setName("name" + i)
                    .setUsername(userName)
                    .setSurname("surname" + i)
                    .setPostalCode("code" + i)
                    .setCountry("country" + i)
                    .setCity("city" + i)
                    .setRoles(roles)
                    .setEmail("email" + i + "@domain.com")
                    .setPassword(passwordEncoder.encode("pass" + i))
                    .setAddress("addr" + i);

            i++;

            users.add(user);
        }

        User user = new User()
                .setName("John")
                .setSurname("Smith")
                .setUsername("johnsmith123")
                .setEmail("johnsmith@gmail.com")
                .setAddress("address")
                .setCity("city")
                .setCountry("country")
                .setPostalCode("postal code")
                .setPassword(passwordEncoder.encode("johnsmith"))
                .setRoles(roles);

        users.add(user);

        edition = editionRepository.findById(edition.getId()).get();
        edition.setParticipants(users);

        userRepository.saveAll(users);
        editionRepository.save(edition);
    }

    private void saveItems() {
        // zapisac item
        for (ItemData itemdata : this.items) {
            User user = userRepository.findByUsername(itemdata.userName).get();
            Item item = new Item()
                    .setEdition(edition)
                    .setUser(user)
                    .setDescription("desc")
                    .setId(itemdata.haveItemId);

            itemRepository.save(item);
        }
    }

    private void saveItemsNotPresent() {

        Set<Item> items = new HashSet<>();
        User user = userRepository.findByUsername("johnsmith123").get();

        for (long i = 1; i <= maxId; i++) {
            if(!itemRepository.existsById(i)) {
                Item item = new Item()
                        .setId(i)
                        .setEdition(edition)
                        .setUser(user)
                        .setName("NPP")
                        .setDescription("no preference product")
                        .setImages(Collections.emptySet());
                items.add(item);
            }
        }
        itemRepository.saveAll(items);
    }

    @Transactional
    void saveDefinedGroups() {
        //zapisac grupsy
        Set<DefinedGroup> localDefinedGroups = new HashSet<>();

        for (DefinedGroupData definedGroupData : definedGroups) {
            DefinedGroup definedGroup = new DefinedGroup()
                    .setName(definedGroupData.getDefinedGruopName())
                    .setUser(userRepository.findByUsername(definedGroupData.getUserName()).get())
                    .setEdition(edition)
                    .setItems(definedGroupData.getItems().stream().map(e -> itemRepository.findById(e).get()).collect(Collectors.toSet()));
            localDefinedGroups.add(definedGroup);
        }

        for(DefinedGroup definedGroup: localDefinedGroups) {
            Set<Item> items = definedGroup.getItems();
            definedGroup.setItems(null);
            DefinedGroup savedGroup = definedGroupRepository.save(definedGroup);
            savedGroup.setItems(items);
            definedGroupRepository.save(savedGroup);
        }

        System.out.println("First step of saving defined groups terminated successfully");

        //trzeba teraz zapisac grupy w grupach
        //posiadamy usernamea i nazwy grup
        //musimy wyciagnac grupy o tych nazwach ktore naleza tez do tego usera

        for(DefinedGroupData definedGroupData: definedGroups) {

            String definedGruopName = definedGroupData.getDefinedGruopName();
            String userName = definedGroupData.getUserName();
            System.out.println("Group name: " + definedGruopName + "  User name: " + userName);

            if(definedGroupData.getDefinedGroups().size()!= 0) {
                int i = 1;
            }

            DefinedGroup definedGroup = definedGroupRepository.findByNameAndUser_Username(definedGruopName, userName);

            for(String groupName: definedGroupData.getDefinedGroups()) {
                DefinedGroup groupToAdd = definedGroupRepository.findByNameAndUser_Username(groupName, userName);
                definedGroup.getGroups().add(groupToAdd);
            }
            definedGroupRepository.save(definedGroup);
        }


//        // hacks for updating list of defined groups for defined groups
//        List<DefinedGroup> repositoryDefinedGroups = definedGroupRepository.findAll();
//        for (DefinedGroup definedGroup : repositoryDefinedGroups) {
//
//
//
//            DefinedGroupData definedGroupData = definedGroups.stream().filter(df -> df.getDefinedGruopName().equals(definedGroup.getName())).findFirst().get();
//            definedGroup.setGroups(repositoryDefinedGroups.stream().filter(df -> definedGroupData.getDefinedGroups().contains(df.getName())).collect(Collectors.toSet()));
//        }
//
//        definedGroupRepository.saveAll(repositoryDefinedGroups);
    }

    private void savePreferences() {
        //zapisac prefki

        Set<Preference> localPreferences = new HashSet<>();

        for (PreferenceLocal preferenceLocal : preferences) {
            HashSet<DefinedGroup> groups = new HashSet<>();
            preferenceLocal.definedGroups.forEach(definedGroupName -> {
                DefinedGroup definedGroup = definedGroupRepository.findByNameAndUser_Username(definedGroupName, preferenceLocal.userName);
                groups.add(definedGroup);
            });

            Preference preference = new Preference()
                    .setEdition(edition)
                    .setHaveItem(itemRepository.findById(preferenceLocal.haveItemId).get())
                    .setUser(userRepository.findByUsername(preferenceLocal.userName).get())
                    .setWantedDefinedGroups(groups)
                    .setWantedItems(itemRepository.findAllByIdIn(preferenceLocal.wantedItems));
            localPreferences.add(preference);
        }

        preferenceRepository.saveAll(localPreferences);
    }

    private void parsePreference(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");
        userNames.add(userName);

        if(userName.equals("razul")){
            int i = 1;
        }
        Long haveItemId = Long.valueOf(leftSideOfCollon[1].trim());
        items.add(new ItemData(userName, haveItemId));
        Set<String> wantedDefinedGroups = new HashSet<>();
        Set<Long> wantedItems = new HashSet<>();

        for (String wantedObject : rightSideOfCollon) {
            switch (wantedObject.trim().charAt(0)) {
                case '%':
                    wantedDefinedGroups.add(getDefinedGroupName(wantedObject));
                    break;
                default:
                    long id = Long.parseLong(wantedObject);
                    if (id > maxId) {
                        maxId = id;
                    }
                    wantedItems.add(id);
//                    items.add(new ItemData(null, id));
                    break;
            }
        }

        preferences.add(new PreferenceLocal(new Preference(), wantedDefinedGroups, userName, haveItemId, wantedItems));

    }

    private void parseDefinedGroupDefinition(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");
        userNames.add(userName);
        String definedGroupName = getDefinedGroupName(leftSideOfCollon[1]);

        Set<String> defindedGroupsDefinedGroups = rightSideOfCollon.stream().filter(e -> e.startsWith("%")).map(this::getDefinedGroupName).collect(Collectors.toSet());

//        definedGroups.addAll(defindedGroupsDefinedGroups.stream().map(e -> new DefinedGroupData(null, e, null,null)).collect(Collectors.toSet()));

        Set<Long> definedGroupsItems = rightSideOfCollon.stream().filter(e -> !e.startsWith("%")).map(e -> Long.valueOf(e.trim())).collect(Collectors.toSet());

//        items.addAll(definedGroupsItems.stream().map(id -> new ItemData(null, id)).collect(Collectors.toSet()));

        definedGroups.add(new DefinedGroupData(userName, definedGroupName, definedGroupsItems, defindedGroupsDefinedGroups));

    }

    private String getDefinedGroupName(String s) {
        return s.replace("%", "");
    }

    private class ItemData {
        String userName;
        Long haveItemId;

        ItemData(String userName, Long haveItemId) {

            this.userName = userName;
            this.haveItemId = haveItemId;
        }
    }

    private class PreferenceLocal {
        Preference preference;
        Set<String> definedGroups;
        String userName;
        Long haveItemId;
        Set<Long> wantedItems;

        public PreferenceLocal(Preference preference, Set<String> definedGroups, String userName, Long haveItemId, Set<Long> wantedItems) {
            this.preference = preference;
            this.definedGroups = definedGroups;
            this.userName = userName;
            this.haveItemId = haveItemId;
            this.wantedItems = wantedItems;
        }

    }
}
