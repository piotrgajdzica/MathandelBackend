package mathandel.backend.component;

import mathandel.backend.model.server.Preference;
import mathandel.backend.service.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class MathhandelDataPopulator {
    public static void main(String[] args) throws IOException {
        new MathhandelDataPopulator().parseFile("/home/monikeu/Dokumenty/mathandel-back/MathandelBackend/src/main/resources/mathandel_example_preference_data/mathandel_30.txt");
    }

    private Set<ItemData> items = new HashSet<>();
    private Set<DefinedGroupData> definedGroups = new HashSet<>();
    private Set<PreferenceLocal> preferences;
//    private Map<ItemData, Set<Long>> userItemPreferences = new HashMap<>();
//    private Map<ItemData, Set<String>> userDefinedGroupPreferences = new HashMap<>();

    public void parseFile(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line = bufferedReader.readLine();

        while (line != null) {
            String[] splitLine = line.split(":");
            String[] leftSideOfCollon = splitLine[0].trim().split(" ");
            Set<String> res = new HashSet<>();
            Set<String> rightSideOfCollon = Arrays.stream(splitLine[1].trim().split(";")).flatMap(e -> Arrays.stream(e.split(" "))).collect(Collectors.toSet());

            switch (leftSideOfCollon[1].charAt(0)) {
                case '%':
                    parseDefinedGroupDefinition(leftSideOfCollon, rightSideOfCollon);
                    break;

                default:
                    parsePreference(leftSideOfCollon, rightSideOfCollon);
                    break;
            }
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

        Long haveItemId = Long.valueOf(leftSideOfCollon[1]);
        items.add(new ItemData(userName, haveItemId));
        Set<String> wantedDefinedGroups = new HashSet<>();
        Set<Long> wantedItems = new HashSet<>();

        for (String wantedObject : rightSideOfCollon) {
            switch (wantedObject.charAt(0)) {
                case '%':
                    wantedDefinedGroups.add(wantedObject.replace("%", ""));
                    break;
                default:
                    long id = Long.parseLong(wantedObject);
                    wantedItems.add(id);
                    items.add(new ItemData(null, id));
                    break;
            }
        }

        preferences.add(new PreferenceLocal(new Preference(),wantedDefinedGroups, userName,haveItemId,  wantedItems));

    }

    private void parseDefinedGroupDefinition(String[] leftSideOfCollon, Set<String> rightSideOfCollon) {
        String userName = leftSideOfCollon[0].replace("(", "").replace(")", "");

        String definedGroupName = leftSideOfCollon[1].replace("%", "");

        Set<Long> definedGroupsItems = rightSideOfCollon.stream().map(Long::valueOf).collect(Collectors.toSet());

        items.addAll(definedGroupsItems.stream().map(id -> new ItemData(null, id)).collect(Collectors.toSet()));

        definedGroups.add(new DefinedGroupData(userName,definedGroupName,definedGroupsItems));

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


        DefinedGroupData(String userName, String definedGruopName, Set<Long> items) {
            this.userName = userName;
            this.definedGruopName = definedGruopName;
            this.items = items;
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
