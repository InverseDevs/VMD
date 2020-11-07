package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        StringBuilder data = new StringBuilder();
        try {
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new IOException("Error while parsing http request, " + this.getClass() + ", register");
        }
        JSONObject jsonObject = new JSONObject(data.toString());
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        String birthTown = jsonObject.getString("birth_town");
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("birth_date"));

        User user = new User(username, password, email, name, birthTown, birthDate);

        JSONObject result = new JSONObject();
        if (!userService.saveUser(user)) {
            result.put("status", "user already exists");
        } else {
            MailSender mailSender = new MailSender();
            mailSender.sendVerification(user);

            result.put("status", "registered");
        }

        return result.toString();
    }

    @RequestMapping(value = "/verification/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String verify(@PathVariable("token") String token) {
        userService.permitUser(token);

        JSONObject result = new JSONObject();
        result.put("status", "verified");

        return result.toString();
    }

    //TODO Удалить всё, что идёт ниже

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }
}