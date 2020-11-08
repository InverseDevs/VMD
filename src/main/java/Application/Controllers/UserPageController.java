package Application.Controllers;

import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import Application.Services.WallPostService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders="Authorization")
@Controller
@Slf4j
public class UserPageController {
    @Autowired
    private WallPostService postService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/posts/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String getPosts(@PathVariable("token") String token, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserByToken(token);
                Iterable<WallPost> posts = postService.allUserPagePosts(user.getId());

                int idx = 0;
                for (WallPost post : posts) {
                    responseJson.put("post_" + ++idx, post.toJson());
                }
            } else {
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "user not found");
        } catch (WallPostNotFoundException e) {
            responseJson.put("status", "posts not found");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/posts/{token}", method = RequestMethod.POST)
    @ResponseBody
    public String addPost(@PathVariable("token") String token, HttpServletRequest request) {
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

                JSONObject receivedDataJson = new JSONObject(data.toString());
                String sender = receivedDataJson.getString("sender");
                String content = receivedDataJson.getString("content");
                User user = (User) userService.loadUserByUsername(sender);

                postService.addPost(new WallPost(
                        user,
                        content,
                        new Date(),
                        userService.findUserByToken(token).getId(),
                        WallPost.PageType.USER));

                responseJson.put("status", "success");
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
