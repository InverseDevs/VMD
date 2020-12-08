package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Exceptions.User.Exist.UserAlreadyExists;
import Application.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
            JSONObject receivedDataJson = new JSONObject(data.toString());

            User user = userService.createUser(
                    receivedDataJson.getString("username"),
                    receivedDataJson.getString("password"),
                    receivedDataJson.getString("email"));
            MailSender mailSender = new MailSender();
            mailSender.sendVerification(user);

            responseJson.put("status", "success");
        } catch (UserAlreadyExists e) {
            log.error("user already exists: " + e.getMessage());
            responseJson.put("status", "user already exists");
        } catch (JSONException | IOException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (MessagingException e) {
            log.error("incorrect email: " + e.getMessage());
            responseJson.put("status", "incorrect email");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/verification/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String verify(@PathVariable("id") Long id) {
        JSONObject responseJson = new JSONObject();
        try {
            userService.permitUser(id);
            responseJson.put("status", "success");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}