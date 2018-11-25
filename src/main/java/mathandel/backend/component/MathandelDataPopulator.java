package mathandel.backend.component;

import mathandel.backend.model.server.*;
import mathandel.backend.model.server.enums.RoleName;
import mathandel.backend.repository.*;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MathandelDataPopulator {

    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private DefinedGroupRepository definedGroupRepository;
    private PreferenceRepository preferenceRepository;
    private RoleRepository roleRepository;
    private EditionRepository editionRepository;

    private Set<String> userNames = new HashSet<>();
    private Set<ItemData> items = new HashSet<>();
    private Set<DefinedGroupData> definedGroups = new HashSet<>();
    private Set<PreferenceLocal> preferences = new HashSet<>();

    private Edition edition;

    public MathandelDataPopulator(UserRepository userRepository, ItemRepository itemRepository, DefinedGroupRepository definedGroupRepository, PreferenceRepository preferenceRepository, RoleRepository roleRepository, EditionRepository editionRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.preferenceRepository = preferenceRepository;
        this.roleRepository = roleRepository;
        this.editionRepository = editionRepository;
    }

    public void saveFromFile(String fileName) throws IOException {
        initEdition();

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
            System.out.println("Parsed " + line);
        }

        saveDataToDb();

    }

    private void initEdition() {
        // todo init edition
        Set<User> mods = new HashSet<>();
        mods.add(userRepository.findById(1L).get());
        edition = new Edition()
                .setName("Mathandel test ")
                .setDescription("desc")
                .setMaxParticipants(Integer.MAX_VALUE)
                .setModerators(mods);

        editionRepository.save(edition);
    }

    private void saveDataToDb() {
        saveUsers();
        saveItems();
        saveDefinedGroups();
        savePreferences();
    }

    private void savePreferences() {
        //zapisac prefki

        Set<Preference> localPreferences = new HashSet<>();

        for (PreferenceLocal preferenceLocal : preferences) {
            Preference preference = new Preference()
                    .setEdition(edition)
                    .setHaveItem(itemRepository.findById(preferenceLocal.haveItemId).get())
                    .setUser(userRepository.findByUsername(preferenceLocal.userName).get())
                    .setWantedDefinedGroups(definedGroupRepository.findAllByNameIn(preferenceLocal.definedGroups))
                    .setWantedItems(itemRepository.findAllByIdIn(preferenceLocal.wantedItems));
            localPreferences.add(preference);
        }

        preferenceRepository.saveAll(localPreferences);
    }

    private void saveDefinedGroups(){
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

        definedGroupRepository.saveAll(localDefinedGroups);


        // hacks for updating list of defined groups for defined groups
        List<DefinedGroup> repositoryDefinedGroups = definedGroupRepository.findAll();
        for (DefinedGroup definedGroup : repositoryDefinedGroups) {
            DefinedGroupData definedGroupData = definedGroups.stream().filter(df -> df.getDefinedGruopName().equals(definedGroup.getName())).findFirst().get();
            definedGroup.setGroups(repositoryDefinedGroups.stream().filter(df -> definedGroupData.getDefinedGroups().contains(df.getName())).collect(Collectors.toSet()));
        }

        definedGroupRepository.saveAll(repositoryDefinedGroups);
    }

    private void saveItems() {
        // zapisac item
        Set<Item> itemsy = new HashSet<>();

        for (ItemData itemdata : items) {
            Item item = new Item()
                    .setEdition(edition)
                    .setUser(userRepository.findByUsername(itemdata.userName).get())
                    .setDescription("desc")
                    .setId(itemdata.haveItemId);

            itemsy.add(item);
        }

        itemRepository.saveAll(itemsy);
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
                    .setEmail("email" + i+"@domain.com")
                    .setPassword("pass" + i)
                    .setAddress("addr" + i);

            i++;

            users.add(user);
        }

        edition = editionRepository.findById(edition.getId()).get();
        edition.setParticipants(users);

        userRepository.saveAll(users);
        editionRepository.save(edition);
    }

    private void parsePreference(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");
        userNames.add(userName);

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
