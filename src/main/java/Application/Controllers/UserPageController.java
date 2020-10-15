package Application.Controllers;

import Application.Content.UserInfo;
import Application.Content.WallPost;
import Application.Entities.User;
import Application.Services.UserInfoService;
import Application.Services.UserService;
import Application.Services.WallPostService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
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
    private UserInfoService infoService;
    private WallPostService postService;
    private UserService userService;

    @Autowired
    public UserPageController(UserInfoService infoService, WallPostService postService, UserService userService) {
        this.infoService = infoService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String userPage(@PathVariable("token") String token, Model model) {
        User user = this.getUserByToken(token);
        UserInfo info = infoService.findUserInfo(user.getId());
        Iterable<WallPost> posts = postService.allUserpagePosts(user.getId());

        model.addAttribute("curr_info", info);
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
        post.setPageId(this.getUserByToken(token).getId());
        post.setPageType(WallPost.PageType.USER);
        post.setSender(sender_username);
        post.setSentTime(new Date());
        postService.addPost(post);
        return "redirect:/user/{token}";
    }

    private User getUserByToken(String token) {
        if(token.startsWith("id")) return userService.findUserById(Long.parseLong(token.substring(2)));
        else return (User) userService.loadUserByUsername(token);
    }
}
