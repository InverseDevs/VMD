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

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User userToDelete = userService.findUserById(id);

        userService.deleteUser(userToDelete.getId());

        return "redirect:/admin";
    }

    @GetMapping("/admin/makeAdmin/{id}")
    public String makeAdmin(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        userService.makeAdmin(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/makeUser/{id}")
    public String makeUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        userService.makeUser(user);

        return "redirect:/admin";
    }
}