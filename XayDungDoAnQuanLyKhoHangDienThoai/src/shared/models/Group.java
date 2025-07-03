package shared.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String name;
    private List<String> members;

    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMember(String username) {
        members.add(username);
    }

    public List<String> getMembers() {
        return members;
    }

    public boolean contains(String username) {
        return members.contains(username);
    }
}
