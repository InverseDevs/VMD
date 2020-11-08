package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders="Authorization")
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/authorization", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        try {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
            JSONObject receivedDataJson = new JSONObject(data.toString());
            String username = receivedDataJson.get("username").toString();
            String password = receivedDataJson.get("password").toString();

            User user = (User) userService.loadUserByUsername(username);

            if (user.getPermitted() && password.equals(user.getPassword())) {
                String token = JwtProvider.generateToken(username, password);
                response.setHeader("Authorization", "Bearer " + token);

                responseJson = user.toJson();
            } else {
                responseJson.put("status", "incorrect password");
            }

        } catch (JSONException | IOException e) {
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
    @ResponseBody
    public String forgotPassword(HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                data.append(line);
            }
            JSONObject receivedDataJson = new JSONObject(data.toString());

            User user = userService.findUserByEmail(receivedDataJson.getString("email"));

            MailSender mailSender = new MailSender();
            mailSender.sendPassword(user);

            responseJson.put("status", "success");
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
}
