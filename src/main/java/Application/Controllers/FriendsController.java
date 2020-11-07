package Application.Controllers;

import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
public class FriendsController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    public String getFriends(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwt = request.getHeader("Authorization").substring(7);
        JSONObject result = new JSONObject();

        if (JwtProvider.validateToken(jwt)) {
            StringBuilder data = new StringBuilder();
            try {
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                throw new IOException("Error while parsing http request, " + this.getClass() + ", register");
            }
            JSONObject jsonObject = new JSONObject(data.toString());
            String username = jsonObject.getString("username");
            User user = (User) userService.loadUserByUsername(username);

            Set<User> friends = new HashSet<>(user.getFriends());

            int idx = 0;
            for (User friend : friends) {
                JSONObject friendJson = new JSONObject();
                friendJson.put("id", friend.getId());
                friendJson.put("username", friend.getUsername());
                friendJson.put("email", friend.getEmail());
                friendJson.put("name", friend.getName());
                friendJson.put("birth_town", friend.getBirthTown());
                friendJson.put("birth_date", friend.getBirthDate());
                friendJson.put("roles", friend.getRoles().toString());
                friendJson.put("friends", friend.getFriends().toString());

                result.put("friend_" + ++idx, friendJson.toString());
            }
        } else {
            result.put("status", "user not authorized");
        }

        return result.toString();
    }

    @RequestMapping(value = "/friends/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String addFriend(@PathVariable("token") String token,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwt = request.getHeader("Authorization").substring(7);
        JSONObject result = new JSONObject();

        if (JwtProvider.validateToken(jwt)) {
            StringBuilder data = new StringBuilder();
            try {
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                throw new IOException("Error while parsing http request, " + this.getClass() + ", register");
            }
            JSONObject jsonObject = new JSONObject(data.toString());
            String username = jsonObject.getString("username");
            User user = (User) userService.loadUserByUsername(username);
            User friend = userService.findUserByToken(token);

            if (!user.getId().equals(friend.getId()) && !userService.friendExists(user, friend)) {
                userService.addFriend(user, friend);

                //TODO Добавить оповещение пользователю
                userService.addFriend(friend, user);

                result.put("status", "friend added");
            }
        } else {
            result.put("status", "user not authorized");
        }

        return result.toString();
    }
}
