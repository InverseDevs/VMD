package Application.Controllers;

import Application.Controllers.API.Exceptions.CommentNotFoundException;
import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Entities.Content.Comment;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.CommentService;
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
    WallPostService wallPostService;

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
                String sender = receivedDataJson.getString("sender");
                String content = receivedDataJson.getString("content");
                User user = (User) userService.loadUserByUsername(sender);
                WallPost post = wallPostService.postById(post_id);

                Comment comment = new Comment(
                        user,
                        content,
                        LocalDateTime.now(),
                        post);

//                JSONArray jArray = receivedDataJson.getJSONArray("picture");
//                byte[] picture = new byte[jArray.length()];
//
//                if (picture.length > 0) {
//                    for (int i = 0; i < jArray.length(); i++) {
//                        picture[i] = (byte) jArray.getInt(i);
//                    }
//                    // Потом может убрать кодирование в Base64
//                    comment.setPicture(Base64.encodeBase64(picture));
//                }

                commentService.addComment(comment);

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
                String sender = receivedDataJson.getString("sender");
                String content = receivedDataJson.getString("content");
                User user = (User) userService.loadUserByUsername(sender);
                Comment comment = commentService.findById(comment_id);
                WallPost post = wallPostService.postById(comment.getPost().getId());

                Comment newComment = new Comment(
                        user,
                        content,
                        LocalDateTime.now(),
                        post);
                newComment.setReference_comment(
                        comment.getReference_comment() == null ? comment : comment.getReference_comment());

//                JSONArray jArray = receivedDataJson.getJSONArray("picture");
//                byte[] picture = new byte[jArray.length()];
//
//                if (picture.length > 0) {
//                    for (int i = 0; i < jArray.length(); i++) {
//                        picture[i] = (byte) jArray.getInt(i);
//                    }
//                    // Потом может убрать кодирование в Base64
//                    newComment.setPicture(Base64.encodeBase64(picture));
//                }

                commentService.addComment(newComment);

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
                commentService.deleteComment(commentId);

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
}
