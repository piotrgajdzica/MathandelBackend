package mathandel.backend.component;

import mathandel.backend.model.server.Preference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MathhandelDataPopulator {
    public static void main(String[] args) throws IOException {
        new MathhandelDataPopulator().parseFile("/home/monikeu/Dokumenty/mathandel-back/MathandelBackend/src/main/resources/mathandel_example_preference_data/mathandel_29,5.txt");
    }

    private Set<ItemData> items = new HashSet<>();
    private Set<DefinedGroupData> definedGroups = new HashSet<>();
    private Set<PreferenceLocal> preferences = new HashSet<>();
//    private Map<ItemData, Set<Long>> userItemPreferences = new HashMap<>();
//    private Map<ItemData, Set<String>> userDefinedGroupPreferences = new HashMap<>();

    public void parseFile(String fileName) throws IOException {
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

        //todo zapisac itemsy
        // zapisac userow
        // zapisac itemy
        //zapisac grupsy
        //zapisac prefki
        saveDataToDb();

    }

    private void saveDataToDb() {

    }

    private void parsePreference(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");

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
                    items.add(new ItemData(null, id));
                    break;
            }
        }

        preferences.add(new PreferenceLocal(new Preference(), wantedDefinedGroups, userName, haveItemId, wantedItems));

    }

    private void parseDefinedGroupDefinition(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");

        String definedGroupName = getDefinedGroupName(leftSideOfCollon[1]);

        Set<String> defindedGroupsDefinedGroups = rightSideOfCollon.stream().filter(e -> e.startsWith("%")).map(this::getDefinedGroupName).collect(Collectors.toSet());

        definedGroups.addAll(defindedGroupsDefinedGroups.stream().map(e -> new DefinedGroupData(null, e, null,null)).collect(Collectors.toSet()));

        Set<Long> definedGroupsItems = rightSideOfCollon.stream().filter(e -> !e.startsWith("%")).map(e -> Long.valueOf(e.trim())).collect(Collectors.toSet());

        items.addAll(definedGroupsItems.stream().map(id -> new ItemData(null, id)).collect(Collectors.toSet()));

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

    private class DefinedGroupData {
        String userName;
        String definedGruopName;
        Set<Long> items;
        Set<String> definedGroups;


        DefinedGroupData(String userName, String definedGruopName, Set<Long> items, Set<String> definedGroups) {
            this.userName = userName;
            this.definedGruopName = definedGruopName;
            this.items = items;
            this.definedGroups = definedGroups;
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
