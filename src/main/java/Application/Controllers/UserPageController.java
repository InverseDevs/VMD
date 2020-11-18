package Application.Controllers;

import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import Application.Services.WallPostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class UserPageController {
    @Autowired
    private WallPostService postService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getPosts(@PathVariable("id") Long id, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserById(id);
                Iterable<WallPost> posts = postService.allUserPagePosts(user.getId());

                int idx = 0;
                for (WallPost post : posts) {
                    responseJson.put("post_" + ++idx, post.toJson());
                }
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (WallPostNotFoundException e) {
            log.error("posts not found: " + e.getMessage());
            responseJson.put("status", "posts not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String addPost(@PathVariable("id") Long id, HttpServletRequest request) {
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

                JSONArray jArray = receivedDataJson.getJSONArray("picture");
                byte[] picture = new byte[jArray.length()];

                User user = (User) userService.loadUserByUsername(sender);

                WallPost post = new WallPost(
                        user,
                        content,
                        LocalDateTime.now(),
                        userService.findUserById(id).getId(),
                        WallPost.PageType.USER);

                if (picture.length > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        picture[i] = (byte) jArray.getInt(i);
                    }
                    // Потом может убрать кодирование в Base64
                    post.setPicture(Base64.encodeBase64(picture));
                }

                postService.addPost(post);

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
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

    @RequestMapping(value = "/post/delete/{post_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deletePost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                postService.deletePost(postId);

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (WallPostNotFoundException e) {
            log.error("post not found: " + e.getMessage());
            responseJson.put("status", "post not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/like/post/{post_id}", method = RequestMethod.POST)
    @ResponseBody
    public String likePost(@PathVariable("post_id") Long postId, HttpServletRequest request) {
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
                Long userId = receivedDataJson.getLong("userId");
                User user = userService.findUserById(userId);
                WallPost post = postService.postById(postId);

                if (postService.checkLike(post, user)) {
                    postService.like(post, user);
                    responseJson.put("status", "added");
                } else {
                    postService.removeLike(post, user);
                    responseJson.put("status", "removed");
                }
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (JSONException | IOException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (WallPostNotFoundException e) {
            log.error("post not found: " + e.getMessage());
            responseJson.put("status", "post not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}
