package shared.request;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender;
    private String recipient;
    private String message;
    private boolean isGroup;
    private String senderDisplayName;
    private String groupName;

    public ChatMessage(String sender, String senderDisplayName, String recipient, String message, boolean isGroup) {
        this(sender, senderDisplayName, recipient, message, isGroup, null);
    }

    public ChatMessage(String sender, String senderDisplayName, String recipient, String message, boolean isGroup, String groupName) {
        this.sender = sender;
        this.senderDisplayName = senderDisplayName;
        this.recipient = recipient;
        this.message = message;
        this.isGroup = isGroup;
        this.groupName = groupName;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return (isGroup ? "[Nhóm: " + groupName + "]" : "[Chat]") + " " + sender + " -> " + recipient + ": " + message;
    }
}
