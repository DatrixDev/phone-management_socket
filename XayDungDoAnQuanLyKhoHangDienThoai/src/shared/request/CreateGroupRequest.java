package shared.request;

import java.io.Serializable;
import java.util.List;

public class CreateGroupRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String groupName;
    private final List<String> members;

    public CreateGroupRequest(String groupName, List<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getMembers() {
        return members;
    }
}
