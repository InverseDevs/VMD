package Application.Controllers;

import Application.Entities.User;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FriendsController {

    @Autowired
    UserService userService;

    @GetMapping("/friends")
    public String getFriends(Model model) {
        User currentUser = (User) userService.loadUserByUsername(
                String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));

        List<User> friends = new ArrayList<>(currentUser.getFriends());
        model.addAttribute("friends", friends.toArray());

        return "friends";
    }

    @GetMapping("/friends/{token}")
    public String addFriend(@PathVariable("token") String token, Model model) {
        User currentUser = (User) userService.loadUserByUsername(
                String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        User friend = userService.findUserByToken(token);

        if (!currentUser.getId().equals(friend.getId()) && !userService.friendExists(currentUser, friend)) {
            userService.addFriend(currentUser, friend);

            //TODO Добавить оповещение пользователю
            userService.addFriend(friend, currentUser);

        }

        return "redirect:/";
    }
}
