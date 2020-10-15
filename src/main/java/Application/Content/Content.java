package Application.Content;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Content {
    private Long id;
    private String sender;
    private String content;
    private Date sentTime;
}
