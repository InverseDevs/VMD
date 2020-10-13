package Application.Controllers;

import Application.Content.UserInfo;
import Application.Services.UserInfoService;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserPageController {
    private UserInfoService infoService;

    @Autowired
    public UserPageController(UserInfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/user/{token}")
    public String userPage(@PathVariable("token") String token, Model model) {
        UserInfo info;
        if(token.startsWith("id")) info = infoService.findUserInfo(Long.parseLong(token.substring(2)));
        else info = infoService.findUserInfo(token);

        model.addAttribute("curr_info", info);
        return "userPage";
    }
}
