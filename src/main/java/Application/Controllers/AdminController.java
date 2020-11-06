package Application.Controllers;

import Application.Entities.User;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.stream.StreamSupport;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String getUserList(Model model) {
        Iterable<User> users = userService.allUsers();

        model.addAttribute("users", StreamSupport.stream(users.spliterator(), false).toArray());

        return "admin";
    }

    @GetMapping("/admin/delete/{token}")
    public String deleteUser(@PathVariable("token") String token) {
        User userToDelete = userService.findUserByToken(token);

        userService.deleteUser(userToDelete.getId());

        return "redirect:/admin";
    }

    @GetMapping("/admin/makeAdmin/{token}")
    public String makeAdmin(@PathVariable("token") String token) {
        User user = userService.findUserByToken(token);

        userService.makeAdmin(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/makeUser/{token}")
    public String makeUser(@PathVariable("token") String token) {
        User user = userService.findUserByToken(token);

        userService.makeUser(user);

        return "redirect:/admin";
    }
}