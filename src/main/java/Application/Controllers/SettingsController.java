package Application.Controllers;

import Application.Content.UserInfo;
import Application.Services.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController {
    private UserInfoService infoService;
    private Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    public SettingsController(UserInfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping
    public String settings(Model model) {
        String username = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserInfo currInfo = infoService.findUserInfo(username);

        model.addAttribute("userInfo", currInfo);
        return "settings";
    }

    @PostMapping
    public String changeSettings(@ModelAttribute("userInfo") UserInfo updatedInfo, Errors errors) throws ParseException {
        if (errors.hasErrors())
        {
            logger.info(errors.toString());
            return "settings";
        }
        infoService.updateUserInfo(updatedInfo);
        return "redirect:/";
    }
}
