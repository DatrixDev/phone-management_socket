package shared.request;

import java.io.Serializable;

public class HistoryRequest implements Serializable {
    private String fromUser;
    private String toUser;
    private boolean isGroup;

    public HistoryRequest(String fromUser, String toUser, boolean isGroup) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isGroup = isGroup;
    }

    public String getFromUser() { return fromUser; }
    public String getToUser() { return toUser; }
    public boolean isGroup() { return isGroup; }
}
