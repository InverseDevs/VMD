package Application.Controllers;

import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
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
import java.util.HashSet;
import java.util.Set;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
@Controller
public class FriendsController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getFriends(@PathVariable("id") Long id, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserById(id);
                Set<User> friends = new HashSet<>(user.getFriends());
                Set<User> friendRequests = new HashSet<>(user.getFriendRequests());

                JSONArray friendsArray = new JSONArray();

                for (User friend : friends) {
                    friendsArray.put(friend.toJson());
                }

                responseJson.put("friends", friendsArray);

                JSONArray friendsRequestsArray = new JSONArray();

                for (User friendRequest : friendRequests) {
                    friendsRequestsArray.put(friendRequest.toJson());
                }

                responseJson.put("friends_requests", friendsRequestsArray);
            } else {
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String addFriend(@PathVariable("id") Long id, HttpServletRequest request) {
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
                long currentUserId = jsonObject.getLong("id");
                User user = userService.findUserById(currentUserId);
                User friend = userService.findUserById(id);

                if (!user.getId().equals(friend.getId()) && !userService.friendExists(user, friend)) {

                    userService.addFriendRequest(user, friend);

                    responseJson.put("status", "success");
                } else {
                    log.info("cannot create friend");
                    responseJson.put("status", "cannot create friend");
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

    @RequestMapping(value = "/friends/accept/{userToAcceptId}", method = RequestMethod.POST)
    @ResponseBody
    public String acceptFriend(@PathVariable("userToAcceptId") Long userToAcceptId, HttpServletRequest request) {
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
                long currentUserId = jsonObject.getLong("id");
                User friend1 = userService.findUserById(currentUserId);
                User friend2 = userService.findUserById(userToAcceptId);

                if (!friend1.getId().equals(friend2.getId()) && !userService.friendExists(friend1, friend2)) {

                    userService.makeFriends(friend1, friend2);
                    userService.deleteFriendRequest(friend1, friend2);

                    responseJson.put("status", "success");
                } else {
                    log.info("cannot create friend");
                    responseJson.put("status", "cannot create friend");
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

    @RequestMapping(value = "/friends/decline/{userToDeclineId}", method = RequestMethod.POST)
    @ResponseBody
    public String declineFriend(@PathVariable("userToDeclineId") Long userToDeclineId, HttpServletRequest request) {
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
                long currentUserId = jsonObject.getLong("id");
                User user = userService.findUserById(currentUserId);
                User friend = userService.findUserById(userToDeclineId);

                if (!user.getId().equals(friend.getId()) && !userService.friendExists(user, friend)) {

                    userService.deleteFriendRequest(user, friend);

                    responseJson.put("status", "success");
                } else {
                    log.info("cannot create friend");
                    responseJson.put("status", "cannot create friend");
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

    @RequestMapping(value = "/friends/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String removeFriend(@PathVariable("id") Long id, HttpServletRequest request) {
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
                long currentUserId = jsonObject.getLong("id");
                User friend1 = userService.findUserById(currentUserId);
                User friend2 = userService.findUserById(id);

                if (!friend1.getId().equals(friend2.getId()) && userService.friendExists(friend1, friend2)) {

                    userService.deleteFriends(friend1, friend2);

                    responseJson.put("status", "success");
                } else {
                    log.info("cannot remove friend");
                    responseJson.put("status", "cannot remove friend");
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
}
