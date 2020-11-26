package Application.Controllers;

import Application.Controllers.API.Exceptions.WallPost.WallPostNotFoundException;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import Application.Services.WallService;
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

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class UserPageController {
    @Autowired
    private WallService wallService;
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
                //Iterable<WallPost> posts = postService.allUserPagePosts(user.getId());
                Iterable<WallPost> posts = wallService.findAllUserPagePosts(user);

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
                String picture = receivedDataJson.getString("picture");
                User user = (User) userService.loadUserByUsername(sender);

//                postService.addPost(new WallPost(
//                        user,
//                        content,
//                        LocalDateTime.now(),
//                        userService.findUserById(id).getId(),
//                        WallPost.PageType.USER));
                wallService.addPost(user, content, userService.findUserById(id), picture.getBytes());

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
                //postService.deletePost(postId);
                wallService.deletePostById(postId);

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
                //WallPost post = postService.postById(postId);
                WallPost post = wallService.findPostById(postId);

//                if (postService.checkLike(post, user)) {
//                    postService.like(post, user);
//                    responseJson.put("status", "added");
//                } else {
//                    postService.removeLike(post, user);
//                    responseJson.put("status", "removed");
//                }
                if (wallService.checkLike(post, user)) {
                    wallService.like(post, user);
                    responseJson.put("status", "added");
                } else {
                    wallService.removeLike(post, user);
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
