package Application.Controllers;

import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
public class FriendsController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/friends/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String getFriends(@PathVariable("token") String token, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserByToken(token);
                Set<User> friends = new HashSet<>(user.getFriends());

                int idx = 0;
                for (User friend : friends) {
                    responseJson.put("friend_" + ++idx, friend.toJson());
                }
            } else {
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/friends/{token}", method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(@PathVariable("token") String token, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }

                JSONObject jsonObject = new JSONObject(data.toString());
                String username = jsonObject.getString("username");
                User user = (User) userService.loadUserByUsername(username);
                User friend = userService.findUserByToken(token);

                if (!user.getId().equals(friend.getId()) && !userService.friendExists(user, friend)) {
                    userService.addFriend(user, friend);

                    //TODO Добавить оповещение пользователю
                    userService.addFriend(friend, user);

                    responseJson.put("status", "success");
                }
            } else {
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            responseJson.put("status", "incorrect request headers");
        } catch (JSONException | IOException e) {
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}
