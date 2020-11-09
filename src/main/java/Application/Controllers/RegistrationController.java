package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
            User user = new User(
                    receivedDataJson.getString("username"),
                    receivedDataJson.getString("password"),
                    receivedDataJson.getString("email"),
                    receivedDataJson.getString("name"),
                    receivedDataJson.getString("birth_town"),
                    new SimpleDateFormat("yyyy-MM-dd").parse(receivedDataJson.getString("birth_date")));

            if (!userService.saveUser(user)) {
                responseJson.put("status", "user already exists");
            } else {
                MailSender mailSender = new MailSender();
                mailSender.sendVerification(user);

                responseJson.put("status", "success");
            }
        } catch (JSONException | IOException e) {
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (MessagingException e) {
            responseJson.put("status", "incorrect email");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/verification/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String verify(@PathVariable("token") String token) {
        userService.permitUser(token);
        return "verification";
    }
}