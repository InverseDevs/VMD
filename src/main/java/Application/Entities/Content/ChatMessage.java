package Application.Entities.Content;

import Application.Entities.Chat;
import Application.Entities.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public ChatMessage(String message, LocalDateTime sentTime, User sender, Chat chat) {
        super(sender, message, sentTime);
        this.chat = chat;
    }

    public JSONObject toJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("message_id", this.getId() == null ? "" : this.getId());
        resultJson.put("chat_id", this.getChat() == null ? "" : this.getChat().getId());
        resultJson.put("sender_id", this.getSender() == null ? "" : this.getSender());
        resultJson.put("message", this.getContent() == null ? "" : this.getContent());
        resultJson.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());

        return resultJson;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ChatMessage)) return false;
        ChatMessage msg = (ChatMessage) o;
        return this.getSender().equals(msg.getSender()) &&
                this.getSentTime().equals(msg.getSentTime()) &&
                this.getContent().equals(msg.getContent()) &&
                this.chat.equals(msg.chat);
    }
}