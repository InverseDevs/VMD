package Application.Content;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class Content {
    @Id
    @GeneratedValue
    private Long id;

    private String sender;
    @Column(name = "message")
    private String content;
    private Date sentTime;
}
