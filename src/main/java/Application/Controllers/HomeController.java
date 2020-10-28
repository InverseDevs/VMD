package Application.Controllers;

import Application.Entities.User;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.StreamSupport;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getHome(Model model) {
        Iterable<User> users = userService.allUsers();

        model.addAttribute("users", StreamSupport.stream(users.spliterator(), false).toArray());

        return "home";
    }

    @GetMapping("/chattest")
    public String getChatIndex(Model model) {
        return "chatIndex";
    }
}
