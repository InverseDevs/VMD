package Application.Entities.Content;

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

    private String sender;
    @Column(name = "message")
    private String content;
    private Date sentTime;

    public Content(String sender, String content, Date sentTime) {
        this.sender = sender;
        this.content = content;
        this.sentTime = sentTime;
    }
}
