package Application.Controllers;

import Application.Content.UserInfo;
import Application.Content.WallPost;
import Application.Entities.User;
import Application.Services.UserInfoService;
import Application.Services.UserService;
import Application.Services.WallPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
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

    @GetMapping("/user/{token}")
    public String userPage(@PathVariable("token") String token, Model model) {
        UserInfo info;
        User user;

        if(token.startsWith("id")) user = userService.findUserById(Long.parseLong(token.substring(2)));
        else user = (User) userService.loadUserByUsername(token);

        info = infoService.findUserInfo(user.getId());
        Iterable<WallPost> posts = postService.allUserpagePosts(user.getId());

        model.addAttribute("curr_info", info);
        model.addAttribute("posts", posts);
        return "userPage";
    }
}
