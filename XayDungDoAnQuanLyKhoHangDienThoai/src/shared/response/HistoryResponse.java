package shared.response;
import shared.request.ChatMessage;

import java.io.Serializable;
import java.util.List;

public class HistoryResponse implements Serializable {
    private List<
            ChatMessage> messages;

    public HistoryResponse(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
}
