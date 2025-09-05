package shared.response;

import java.io.Serializable;

public class DeleteMessageBroadcast implements Serializable {
    private final String conversationId;
    private final String messageId;
    private final boolean deleteForAll;

    public DeleteMessageBroadcast(String conversationId, String messageId, boolean deleteForAll) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.deleteForAll = deleteForAll;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isDeleteForAll() {
        return deleteForAll;
    }
}
