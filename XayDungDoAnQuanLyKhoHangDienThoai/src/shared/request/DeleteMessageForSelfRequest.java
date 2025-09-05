package shared.request;

import java.io.Serializable;

public class DeleteMessageForSelfRequest implements Serializable {
    private final String conversationId;
    private final String messageId;

    public DeleteMessageForSelfRequest(String conversationId, String messageId) {
        this.conversationId = conversationId;
        this.messageId = messageId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getMessageId() {
        return messageId;
    }
}
