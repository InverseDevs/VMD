package Application.Controllers;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Services.UserService;
import Application.Services.WallPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/user/{token}")
@Slf4j
public class UserPageController {
    @Autowired
    private WallPostService postService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String userPage(@PathVariable("token") String token, Model model) {
        User user = this.getUserByToken(token);
        Iterable<WallPost> posts = postService.allUserpagePosts(user.getId());

        model.addAttribute("currUser", user);
        model.addAttribute("posts", posts);
        model.addAttribute("post", new WallPost());
        return "userPage";
    }

    @PostMapping
    public String writePost(@PathVariable("token") String token,
                            @ModelAttribute WallPost post, Errors errors) {
        // TODO прописать какое-нибудь поведение при попытке отправить пустой пост
        if(post.getContent() == null || post.getContent().equals("")) {
            return "redirect:/user/{token}";
        }
        String sender_username = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User sender = (User) userService.loadUserByUsername(sender_username);
        post.setPageId(this.getUserByToken(token).getId());
        post.setPageType(WallPost.PageType.USER);
        post.setSender(sender);
        post.setSentTime(new Date());
        postService.addPost(post);
        return "redirect:/user/{token}";
    }

    private User getUserByToken(String token) {
        if(token.startsWith("id")) return userService.findUserById(Long.parseLong(token.substring(2)));
        else return (User) userService.loadUserByUsername(token);
    }
}
