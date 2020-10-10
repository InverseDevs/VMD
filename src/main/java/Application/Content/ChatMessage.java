package Application.Content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage extends Content {
    private Long chatId;
    private MessageType type;
    private String receiver;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}