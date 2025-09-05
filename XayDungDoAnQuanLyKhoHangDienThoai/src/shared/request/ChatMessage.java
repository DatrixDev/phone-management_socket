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

    // <-- ID của tin nhắn trong DB (dùng cho xóa/broadcast/history)
    private Long id;

    // Client gửi lên (chưa có id)
    public ChatMessage(String sender, String senderDisplayName,
                       String recipient, String message,
                       boolean isGroup) {
        this(sender, senderDisplayName, recipient, message, isGroup, null, null);
    }

    // Client gửi lên (group có groupName), vẫn chưa có id
    public ChatMessage(String sender, String senderDisplayName,
                       String recipient, String message,
                       boolean isGroup, String groupName) {
        this(sender, senderDisplayName, recipient, message, isGroup, groupName, null);
    }

    // Server/history trả về (đã có id)
    public ChatMessage(Long id,
                       String sender, String senderDisplayName,
                       String recipient, String message,
                       boolean isGroup, String groupName) {
        this(sender, senderDisplayName, recipient, message, isGroup, groupName, id);
    }

    // Constructor nội bộ
    private ChatMessage(String sender, String senderDisplayName,
                        String recipient, String message,
                        boolean isGroup, String groupName, Long id) {
        this.sender = sender;
        this.senderDisplayName = senderDisplayName;
        this.recipient = recipient;
        this.message = message;
        this.isGroup = isGroup;
        this.groupName = groupName;
        this.id = id;
    }

    // Getters
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getSenderDisplayName() { return senderDisplayName; }
    public String getMessage() { return message; }
    public boolean isGroup() { return isGroup; }
    public String getGroupName() { return groupName; }
    public Long getId() { return id; }

    // Setter cho server gán id sau khi lưu DB
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        String scope = isGroup ? "[Group: " + groupName + "]" : "[Chat]";
        String idPart = (id != null ? " (id=" + id + ")" : "");
        return scope + idPart + " " + sender + " -> " + recipient + ": " + message;
    }
}
