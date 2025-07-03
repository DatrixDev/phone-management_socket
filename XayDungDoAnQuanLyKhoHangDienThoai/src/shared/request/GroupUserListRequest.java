package shared.request;


import java.io.Serializable;

public class GroupUserListRequest implements Serializable {
    private final String groupName;

    public GroupUserListRequest(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
