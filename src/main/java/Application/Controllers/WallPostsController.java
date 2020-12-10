package Application.Controllers;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Exceptions.NotEnoughPermissionsException;
import Application.Exceptions.WallPost.WallPostNotFoundException;
import Application.Security.JwtProvider;
import Application.Services.GroupService;
import Application.Services.UserService;
import Application.Services.WallService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class WallPostsController {
    @Autowired
    private WallService wallService;
    @Autowired
    private UserService userService;
    @Autowired
    GroupService groupService;

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
                Iterable<WallPost> posts = wallService.findAllUserPagePosts(user);

                List<WallPost> postsList = new ArrayList<>();
                for (WallPost post : posts) {
                    postsList.add(post);
                }

                JSONArray postsArray = new JSONArray();

                Stream<WallPost> postsStream = postsList.stream().sorted(
                        (post1, post2) -> post2.getSentTime().compareTo(post1.getSentTime()));
                postsStream.forEach(post -> postsArray.put(post.toJson()));

                responseJson.put("posts", postsArray);
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
                String type = receivedDataJson.getString("type");
                User user = (User) userService.loadUserByUsername(sender);

                if (type.equals("user")) {
                    wallService.addPost(
                            user,
                            content,
                            userService.findUserById(id),
                            picture.getBytes());

                    responseJson.put("status", "success");
                } else if (type.equals("group")) {
                    wallService.addPost(
                            user,
                            content,
                            groupService.findGroupById(id),
                            picture.getBytes());

                    responseJson.put("status", "success");
                } else {
                    log.info("unknown type to create post");
                    responseJson.put("status", "unknown type");
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
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }

                User attempter = (User) userService.loadUserByUsername(JwtProvider.getLoginFromToken(jwt));
                wallService.deletePostByIdByUser(postId, attempter);

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
        } catch (NotEnoughPermissionsException e) {
            log.error("no permission: " + e.getMessage());
            responseJson.put("status", "no permission");
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
                WallPost post = wallService.findPostById(postId);

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

    @RequestMapping(value = "/post/picture/{post_id}", method = RequestMethod.POST)
    @ResponseBody
    public String changePostPicture(@PathVariable("post_id") Long postId, HttpServletRequest request) {
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
                String picture = receivedDataJson.getString("picture");

                WallPost post = wallService.findPostById(postId);

                wallService.updatePostPicture(post, picture.getBytes());

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
}
