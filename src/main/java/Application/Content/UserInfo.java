package Application.Content;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserInfo {
    private Long userId;
    private String username;
    private String name;
    private String birthTown;
    private Date birthDate;
}
