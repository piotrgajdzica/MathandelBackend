package mathandel.backend.component;

import java.util.Set;

class DefinedGroupData {
    private String userName;
    private String definedGruopName;
    private Set<Long> items;
    private Set<String> definedGroups;

    public String getUserName() {
        return userName;
    }

    public DefinedGroupData setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getDefinedGruopName() {
        return definedGruopName;
    }

    public DefinedGroupData setDefinedGruopName(String definedGruopName) {
        this.definedGruopName = definedGruopName;
        return this;
    }

    public Set<Long> getItems() {
        return items;
    }

    public DefinedGroupData setItems(Set<Long> items) {
        this.items = items;
        return this;
    }

    public Set<String> getDefinedGroups() {
        return definedGroups;
    }

    public DefinedGroupData setDefinedGroups(Set<String> definedGroups) {
        this.definedGroups = definedGroups;
        return this;
    }


    DefinedGroupData(String userName, String definedGruopName, Set<Long> items, Set<String> definedGroups) {
        this.userName = userName;
        this.definedGruopName = definedGruopName;
        this.items = items;
        this.definedGroups = definedGroups;
    }

}
