package Application.Entities.Content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageAdapter {
    String chat_id;
    String sender_id;
    String message;
}
