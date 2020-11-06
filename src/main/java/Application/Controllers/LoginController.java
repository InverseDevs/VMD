package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/authorization", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder data = new StringBuilder();
        try {
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new IOException("Error while parsing http request, " + this.getClass() + ", login");
        }
        JSONObject jsonObject = new JSONObject(data.toString());
        String username = jsonObject.get("username").toString();
        String password = jsonObject.get("password").toString();

        User user = (User) userService.loadUserByUsername(username);
        JSONObject result = new JSONObject();

        if (user.getPermitted() && password.equals(user.getPassword())) {
            String token = JwtProvider.generateToken(username, password);
            response.setHeader("Authorization", "Bearer " + token);

            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("email", user.getEmail());
            result.put("token", user.getToken());
            result.put("role", user.getRoles().toString());
            result.put("friends", user.getFriends().toString());
        } else {
            result.put("status", "user not found");
        }
        return result.toString();
    }

    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
    @ResponseBody
    public String forgotPassword(HttpServletRequest request) throws IOException {
        StringBuilder data = new StringBuilder();
        try {
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            throw new IOException("Error while parsing http request, " + this.getClass() + ", forgotPassword");
        }
        JSONObject jsonObject = new JSONObject(data.toString());
        User user = userService.findUserByEmail(jsonObject.getString("email"));

        JSONObject result = new JSONObject();

        // TODO Возвращать null-user, а не делать вот так
        if (user.getEmail() != null) {
            MailSender mailSender = new MailSender();
            mailSender.sendPassword(user);

            result.put("status", "ok");
        } else {
            result.put("status", "user not found");
        }

        return result.toString();
    }


    // TODO Удалить всё, что идёт ниже

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", new User());

        return "/login";
    }

    @PostMapping("/exit")
    public String exit() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }

    @GetMapping("/forgot_password")
    public String forgetPassword(Model model) {
        model.addAttribute("user", new User());

        return "forgot_password";
    }
}
