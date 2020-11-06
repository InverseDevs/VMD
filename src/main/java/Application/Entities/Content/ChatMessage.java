package Application.Entities.Content;

import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatMessage extends Content {
    private Long chatId;
    private MessageType type;
    @ManyToOne
    @JoinColumn(name = "user")
    private User receiver;

    public ChatMessage(User sender, String message, Date sentTime, Long chatId) {
        super(sender, message, sentTime);
        this.chatId = chatId;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}