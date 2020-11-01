package Application.Content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_info")
public class UserInfo {
    private final static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    // TODO связать UserInfo и User другим, более ясным с точки зрения архитектуры способом
    @Id
    private Long userId;

    private String username;
    private String name;
    private String birthTown;
    private Date birthDate;

    public String getTextBirthDate() {
        if(birthDate == null) return null;
        else return dateParser.format(birthDate);
    }
    public void setTextBirthDate(String textDate) throws ParseException {
        if(textDate == null) birthDate = null;
        else birthDate = dateParser.parse(textDate);
    }
}
