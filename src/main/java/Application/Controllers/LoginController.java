package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", new User());

        return "/login";
    }

    @PostMapping("/authorization")
    public String postLogin(User user) {
        User userFromDB = (User) userService.loadUserByUsername(user.getUsername());

        if (userFromDB.getPermitted() && user.getPassword().equals(userFromDB.getPassword())) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            userFromDB.getUsername(),
                            userFromDB.getPassword(),
                            userFromDB.getRoles())
            );
            return "redirect:/";
        } else {
            return "/login";
        }
    }

    @PostMapping("/exit")
    public String exit() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }

    @GetMapping("/forgot_password")
    public String forgotPassword(Model model) {
        model.addAttribute("user", new User());

        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String sendPassword(User user) {
        User userFromDB = userService.findUserByEmail(user.getEmail());

        MailSender mailSender = new MailSender();
        mailSender.sendPassword(userFromDB);

        return "/login";
    }
}
