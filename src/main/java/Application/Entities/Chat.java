package Application.Entities;

import Application.Entities.Content.ChatMessage;
import lombok.*;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @OneToMany(mappedBy="chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="chats_to_users",
    joinColumns = @JoinColumn(name = "chat_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    public Chat(Set<User> users) {
        this.users = new HashSet<>(users);
    }

    public Chat(User user1, User user2) {
        this.users = new HashSet<>();
        users.add(user1);
        users.add(user2);
    }

    public JSONObject toJson() {
        JSONObject chatJson = new JSONObject();
        chatJson.put("chat_id", this.getId() == null ? "" : this.getId());
        chatJson.put("picture", this.getPicture() == null ? "" : new String(this.getPicture()));

        JSONObject usersJson = new JSONObject();
        int userIdx = 0;
        for (User user : this.getUsers()) {
            usersJson.put("user_" + ++userIdx, user.toJson());
        }
        chatJson.put("users", usersJson);

        return chatJson;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return this.messages.equals(chat.messages) && this.users.equals(chat.users);
    }
}