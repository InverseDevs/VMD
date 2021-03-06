package Application.Controllers;

import Application.Exceptions.Comment.CommentNotFoundException;
import Application.Exceptions.WallPost.WallPostNotFoundException;
import Application.Entities.Content.Comment;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.CommentService;
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
import java.time.LocalDateTime;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    WallService wallService;

    @RequestMapping(value = "/comment/post/{post_id}", method = RequestMethod.POST)
    @ResponseBody
    public String commentPost(@PathVariable("post_id") Long post_id, HttpServletRequest request) {
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
                String senderFromJson = receivedDataJson.getString("sender");
                String content = receivedDataJson.getString("content");
                String picture = receivedDataJson.getString("picture");
                User sender = (User) userService.loadUserByUsername(senderFromJson);
                WallPost post = wallService.findPostById(post_id);

                commentService.addComment(
                        sender,
                        content,
                        post,
                        null,
                        picture.getBytes());

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
        } catch (WallPostNotFoundException e) {
            log.error("post not found: " + e.getMessage());
            responseJson.put("status", "post not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/comment/comment/{comment_id}", method = RequestMethod.POST)
    @ResponseBody
    public String commentComment(@PathVariable("comment_id") Long comment_id, HttpServletRequest request) {
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
                String userFromJson = receivedDataJson.getString("sender");
                String content = receivedDataJson.getString("content");
                String picture = receivedDataJson.getString("picture");
                User sender = (User) userService.loadUserByUsername(userFromJson);
                Comment comment = commentService.findById(comment_id);
                WallPost post = wallService.findPostById(comment.getPost().getId());

                commentService.addComment(
                        sender,
                        content,
                        post,
                        comment.getReferenceComment() == null ? comment : comment.getReferenceComment(),
                        picture.getBytes());

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
        } catch (WallPostNotFoundException e) {
            log.error("post not found: " + e.getMessage());
            responseJson.put("status", "post not found");
        } catch (CommentNotFoundException e) {
            log.error("comment not found: " + e.getMessage());
            responseJson.put("status", "comment not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/like/comment/{comment_id}", method = RequestMethod.POST)
    @ResponseBody
    public String likeComment(@PathVariable("comment_id") Long commentId, HttpServletRequest request) {
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
                Comment comment = commentService.findById(commentId);

                if (commentService.checkLike(comment, user)) {
                    commentService.like(comment, user);
                    responseJson.put("status", "added");
                } else {
                    commentService.removeLike(comment, user);
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
        } catch (CommentNotFoundException e) {
            log.error("comment not found: " + e.getMessage());
            responseJson.put("status", "comment not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/comment/delete/{comment_id}", method = RequestMethod.POST)
    @ResponseBody
    public String deleteComment(@PathVariable("comment_id") Long commentId, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                commentService.deleteCommentByUser(commentId, (User) userService.loadUserByUsername(JwtProvider.getLoginFromToken(jwt)));

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (CommentNotFoundException e) {
            log.error("comment not found: " + e.getMessage());
            responseJson.put("status", "comment not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/comment/picture/{comment_id}", method = RequestMethod.POST)
    @ResponseBody
    public String changeCommentPicture(@PathVariable("comment_id") Long commentId, HttpServletRequest request) {
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

                Comment comment = commentService.findById(commentId);

                commentService.updatePicture(comment, picture.getBytes());

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
