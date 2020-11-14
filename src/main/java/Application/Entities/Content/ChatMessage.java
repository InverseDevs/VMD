package Application.Entities.Content;

import Application.Entities.Chat;
import Application.Entities.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "messages")
@Entity
public class ChatMessage extends Content {
    @ManyToOne
    @JoinColumn(name="chat_id")
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type;

    public ChatMessage(String message, Date sentTime, User sender, Chat chat) {
        super(sender, message, sentTime);
        this.chat = chat;
    }

    public ChatMessage(String message, Date sentTime, User sender, Chat chat, MessageType type) {
        this(message, sentTime, sender, chat);
        this.type = type;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ChatMessage)) return false;
        ChatMessage msg = (ChatMessage) o;
        return this.getSender().equals(msg.getSender()) &&
                this.getSentTime().equals(msg.getSentTime()) &&
                this.getContent().equals(msg.getContent()) &&
                this.chat.equals(msg.chat) &&
                this.type.equals(msg.type);
    }
}