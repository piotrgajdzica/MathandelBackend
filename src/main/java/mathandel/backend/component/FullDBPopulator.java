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
    private final RateRepository rateRepository;
    private final EditionRepository editionRepository;
    private final DefinedGroupRepository definedGroupRepository;
    private final ProductRepository productRepository;
    private final ResultRepository resultRepository;
    private final TransactionRateRepository transactionRateRepository;
    private final PreferenceRepository preferenceRepository;

    private int editionNumb = 5;
    private int usersNumb = 10;
    private int productNumb = 30;
    private int defifnedGroupsNumb = 20;
    private List<Edition> editions = new LinkedList<>();
    private List<User> users = new LinkedList<>();
    private List<Product> products = new LinkedList<>();
    private List<DefinedGroup> definedGroups = new LinkedList<>();
    private List<Preference> preferences = new LinkedList<>();
    private Random random;


    public FullDBPopulator(RoleRepository roleRepository, UserRepository userRepository, EditionStatusTypeRepository editionStatusTypeRepository, RateRepository rateRepository, EditionRepository editionRepository, DefinedGroupRepository definedGroupRepository, ProductRepository productRepository, ResultRepository resultRepository, TransactionRateRepository transactionRateRepository, PreferenceRepository preferenceRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.editionStatusTypeRepository = editionStatusTypeRepository;
        this.rateRepository = rateRepository;
        this.editionRepository = editionRepository;
        this.definedGroupRepository = definedGroupRepository;
        this.productRepository = productRepository;
        this.resultRepository = resultRepository;
        this.transactionRateRepository = transactionRateRepository;
        this.preferenceRepository = preferenceRepository;
        random = new Random();
    }

    public void populate() {
        createUsers();
        createEditions();
        assignRandomlyUsersToEditions();
        createProducts();
        createDefinedGroups();
        createPreferences();

        save();
    }

    private void save() {
        userRepository.saveAll(users);
        editionRepository.saveAll(editions);
        productRepository.saveAll(products);
        definedGroupRepository.saveAll(definedGroups);
        preferenceRepository.saveAll(preferences);


    }

    private void createUsers() {
        for (int i = 0; i < usersNumb; i++) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("Roles not initialized correctly")));
            User user = new User()
//                    .setId((long) i)
                    .setEmail(randMail())
                    .setName(randName(i))
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
//                    .setId((long) i)
                    .setEditionStatusType(createEditionStatusType())
                    .setEndDate(LocalDate.of(2020, 10, 10))
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

    private void createProducts() {
        for (int i = 0; i < productNumb; i++) {
            Edition edition = getRandEdition();

            while (edition.getParticipants().isEmpty()) {
                edition = getRandEdition();
            }

            User user = edition.getParticipants().iterator().next();
            Product product = new Product()
                    .setName(randName(i))
//                    .setId((long) i)
                    .setDescription(randName(i))
                    .setEdition(edition)
                    .setUser(user);
            products.add(product);
        }
    }

    private void createDefinedGroups() {
        int curProductsNumb = productNumb;
        for (int i = 0; i < defifnedGroupsNumb; i++) {
            Edition edition = getRandEdition();

            while (edition.getParticipants().isEmpty()) {
                edition = getRandEdition();
            }

            User user = getRandUserFromEdition(edition);
            if (user == null) {
                continue;
            }

            int copy = curProductsNumb;

            DefinedGroup definedGroup = new DefinedGroup()
                    .setEdition(edition)
                    .setName(randName(i))
//                    .setId((long) i)
                    .setProducts(new HashSet<>())
                    .setDefinedGroups(new HashSet<>())
                    .setUser(user);


            for (int j = curProductsNumb; j < copy + 3; j++) {
                Product product = new Product()
//                        .setId((long)j)
                        .setName(randName(j))
                        .setDescription(randName(j))
                        .setEdition(edition)
                        .setUser(user);
                products.add(product);
                definedGroup.getProducts().add(product);
                curProductsNumb++;
            }
            curProductsNumb = copy;
            definedGroups.add(definedGroup);

        }
        productNumb += (defifnedGroupsNumb * 3);


        for (int j = 0; j < definedGroups.size() - 1; j++) {
            if (definedGroupBelongToTheSameEdition(definedGroups.get(j), definedGroups.get(j + 1))
                    && definedGroupsBelongToTheSameUser(definedGroups.get(j), definedGroups.get(j + 1))
                    && random.nextInt(2) == 1) {

                definedGroups.get(j).getDefinedGroups().add(definedGroups.get(j + 1));
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
            for (int j = 0; j < products.size() - 1; j++) {
                if (areNeightbourProductsInTheSameEdition(products.get(i), products.get(i + 1))
                        && !productsBelongsToTheSameUser(products.get(i), products.get(i + 1))
                        && random.nextInt(2) == 1) {
                    preferences.add(createPreference(products.get(i), products.get(i + 1)));
                }
            }
            Collections.shuffle(products);
        }


        for (int i = 0; i < products.size() - 1; i++) {
            for (int j = 0; j < definedGroups.size() - 1; j++) {
                if (definedGroupAndProductBelongToTheSameEdition(products.get(i), definedGroups.get(j))
                        && !definedGroupsAndProductBelongToTheSameUser(products.get(i), definedGroups.get(j))
                        && random.nextInt(2) == 1) {

                    if (preferencesOfProduct.containsKey(products.get(i).getId())) {
                        preferencesOfProduct.get(products.get(i).getId()).getWantedDefinedGroups().add(definedGroups.get(j));
                    }
                }
            }

        }

    }

    private boolean definedGroupsAndProductBelongToTheSameUser(Product product, DefinedGroup definedGroup) {
        return product.getUser().getEmail().equals(definedGroup.getUser().getEmail());
    }

    private boolean definedGroupAndProductBelongToTheSameEdition(Product product, DefinedGroup definedGroup) {
        return product.getEdition().getName().equals(definedGroup.getEdition().getName());
    }

    private boolean definedGroupsBelongToTheSameUser(DefinedGroup definedGroup, DefinedGroup definedGroup1) {
        return definedGroup.getUser().getEmail().equals(definedGroup1.getUser().getEmail());
    }

    private boolean definedGroupBelongToTheSameEdition(DefinedGroup definedGroup, DefinedGroup definedGroup1) {
        return definedGroup.getEdition().getName().equals(definedGroup1.getEdition().getName());
    }

    private boolean productsBelongsToTheSameUser(Product product, Product product1) {
        return product.getUser().getEmail().equals(product1.getUser().getEmail());
    }

    private Map<Long, Preference> preferencesOfProduct = new HashMap<>();

    private Preference createPreference(Product product, Product product1) {

        Preference preference = preferencesOfProduct.get(product.getId());
        if (preference == null) {
            preference = new Preference()
                    .setUser(product.getUser())
                    .setEdition(product.getEdition())
                    .setHaveProduct(product)
                    .setWantedProducts(new HashSet<>())
                    .setWantedDefinedGroups(new HashSet<>())
                    .setEdition(product.getEdition());
            preferencesOfProduct.put(product.getId(), preference);
        }
        preference.getWantedProducts().add(product1);

        return preference;
    }

    private boolean areNeightbourProductsInTheSameEdition(Product a, Product b) {
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
