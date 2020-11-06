package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User.User;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(user)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        // TODO внимание костыль!!!! убрать его!!!!!!!!!!!!!!!!!!!!!
        User savedUser = userService.findUserByEmail(user.getEmail());

        MailSender mailSender = new MailSender();
        mailSender.sendVerification(user);

        return "login";
    }

    @GetMapping("/verification/{token}")
    public String verificationToken(@PathVariable("token") String token){
        userService.permitUser(token);

        return "verification";
    }
}