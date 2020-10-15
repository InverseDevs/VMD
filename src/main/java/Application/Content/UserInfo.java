package Application.Content;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class UserInfo {
    private final static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

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
