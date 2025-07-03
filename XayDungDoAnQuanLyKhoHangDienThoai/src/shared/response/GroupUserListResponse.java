package shared.response;

import java.io.Serializable;
import java.util.List;

public class GroupUserListResponse implements Serializable {
    private final String groupName;
    private final List<String> members;

    public GroupUserListResponse(String groupName, List<String> members) {
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
