package shared.request;


import java.io.Serializable;
import java.util.List;

public class UserListUpdate implements Serializable {
    private List<String> onlineUsers;

    public UserListUpdate(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public List<String> getOnlineUsers() {
        return onlineUsers;
    }
}
