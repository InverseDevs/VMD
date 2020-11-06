package Application.Entities.Content;

import Application.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender")
    private User sender;

    @Column(name = "message")
    private String content;
    private Date sentTime;

    public Content(User sender, String content, Date sentTime) {
        this.sender = sender;
        this.content = content;
        this.sentTime = sentTime;
    }
}
