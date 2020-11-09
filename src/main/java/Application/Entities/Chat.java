package Application.Entities;

import Application.Entities.Content.ChatMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long id;

    @OneToMany(mappedBy="chat")
    private List<ChatMessage> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="chats_to_users",
    joinColumns = @JoinColumn(name = "chat_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public Chat(Set<User> users) {
        this.users = new HashSet<>(users);
    }

    public Chat(User user1, User user2) {
        this.users = new HashSet<>();
        users.add(user1);
        users.add(user2);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return this.messages.equals(chat.messages) && this.users.equals(chat.users);
    }
}