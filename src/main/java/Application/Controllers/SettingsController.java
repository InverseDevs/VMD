package Application.Controllers;

import Application.Database.User.UserRepository;
import Application.Entities.User.User;
import Application.Services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    public UserRepository userRepository;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SettingsWrapper {
        private long id;
        private String username;
        private String name;
        private String birthTown;
        private Date birthDate;
        private final static SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

        public SettingsWrapper(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.birthTown = user.getBirthTown();
            this.birthDate = user.getBirthDate();
        }

        public void updateUser(User user) {
            user.setName(name);
            user.setBirthDate(birthDate);
            user.setBirthTown(birthTown);
        }

        public String getTextBirthDate() {
            if(birthDate == null) return null;
            else return dateParser.format(birthDate);
        }
        public void setTextBirthDate(String textDate) throws ParseException {
            if(textDate == null) birthDate = null;
            else birthDate = dateParser.parse(textDate);
        }
    }

    private Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @GetMapping
    public String settings(Model model) {
        String username = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User currUser = userRepository.findByUsername(username);

        model.addAttribute("currUser", new SettingsWrapper(currUser));
        return "settings";
    }

    @PostMapping
    public String changeSettings(@ModelAttribute("currUser") SettingsWrapper settings, Errors errors) throws ParseException {
        if (errors.hasErrors())
        {
            logger.info(errors.toString());
            return "settings";
        }
        User user = userRepository.findById(settings.id).get();
        settings.updateUser(user);
        userRepository.save(user);
        return "redirect:/";
    }
}
