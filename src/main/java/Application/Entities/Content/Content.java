package Application.Entities.Content;

import Application.Entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    @Column(name = "message")
    private String content;
    @Column(name = "sent_time")
    private Date sentTime;

    public Content(User sender, String content, Date sentTime) {
        this.sender = sender;
        this.content = content;
        this.sentTime = sentTime;
    }
}
