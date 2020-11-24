package Application.Controllers;

import Application.Email.MailSender;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
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
                response.addHeader("Access-Control-Expose-Headers", "Authorization");
                response.addHeader("Authorization", "Bearer " + token);

                userService.updateOnline(user, true);

                responseJson = user.toJson();
            } else {
                responseJson.put("status", "incorrect password");
            }

        } catch (JSONException | IOException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
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
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (MessagingException e) {
            log.error("incorrect email: " + e.getMessage());
            responseJson.put("status", "incorrect email");
        } catch (Exception e) {
            log.error("unknown error:" + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/exit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String exit(@PathVariable("id") Long userId, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserById(userId);

                userService.updateOnline(user, false);

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error:" + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}
