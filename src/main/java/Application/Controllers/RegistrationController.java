package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User.User;
import Application.Entities.User.UserInfo;
import Application.Services.UserInfoService;
import Application.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService infoService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        User user = new User(username, password, email);

        JSONObject result = new JSONObject();

        if (!userService.saveUser(user)) {
            result.put("status", "user already exists");
        } else {
            MailSender mailSender = new MailSender();
            mailSender.sendVerification(user);

            result.put("status", "registered");
        }

        // TODO внимание костыль!!!! убрать его!!!!!!!!!!!!!!!!!!!!!
        User savedUser = userService.findUserByEmail(user.getEmail());
        UserInfo info = new UserInfo();
        info.setUsername(savedUser.getUsername());
        info.setUserId(savedUser.getId());
        infoService.updateUserInfo(info);

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