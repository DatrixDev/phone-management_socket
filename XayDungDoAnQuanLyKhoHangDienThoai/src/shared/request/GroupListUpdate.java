package shared.request;

import java.io.Serializable;
import java.util.List;

public class GroupListUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<String> groupNames;

    public GroupListUpdate(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }
}
