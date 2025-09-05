package shared.request;


import java.io.Serializable;

public class DeleteMessageRequest implements Serializable {
    private String conversationId;
    private String sender;
    private String message;
    private boolean isGroup;

    public DeleteMessageRequest(String conversationId, String sender, String message, boolean isGroup) {
        this.conversationId = conversationId;
        this.sender = sender;
        this.message = message;
        this.isGroup = isGroup;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGroup() {
        return isGroup;
    }
}
