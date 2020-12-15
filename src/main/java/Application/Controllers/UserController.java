package Application.Controllers;

import Application.Entities.User;
import Application.Exceptions.User.NoUserFoundException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public String getAllUsers(HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                List<User> users = userService.allUsers();

                JSONArray usersArray = new JSONArray();

                for (User user : users) {
                    usersArray.put(user.toJson());
                }

                responseJson.put("users", usersArray);
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(@PathVariable("id") long id, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserById(id);

                responseJson = user.toJson();
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (NoUserFoundException e) {
            log.error("no users found: " + e.getMessage());
            responseJson.put("status", "no users found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/api/users/change/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String changeSettings(@PathVariable("id") long id, HttpServletRequest request) {
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
                User user = userService.findUserById(id);

                String newName = receivedDataJson.getString("name");
                if (!newName.equals("")) userService.updateName(user, newName);

                String newBirthTown = receivedDataJson.getString("birth_town");
                if (!newBirthTown.equals("")) userService.updateBirthTown(user, newBirthTown);

                String newStudyPlace = receivedDataJson.getString("study_place");
                if (!newStudyPlace.equals("")) userService.updateStudyPlace(user, newStudyPlace);

                String dateString = receivedDataJson.getString("birth_date");
                if (!dateString.equals("")) {
                    LocalDate newBirthDate = LocalDate.parse(dateString);
                    userService.updateBirthDate(user, newBirthDate);
                }

                String newLanguages = receivedDataJson.getString("languages");
                if (!newLanguages.equals("")) userService.updateLanguages(user, newLanguages);

                String newPhoneNumber = receivedDataJson.getString("phone");
                if (!newPhoneNumber.equals("")) userService.updatePhone(user, newPhoneNumber);

                String newHobbies = receivedDataJson.getString("hobbies");
                if (!newHobbies.equals("")) userService.updateHobbies(user, newHobbies);

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (NoUserFoundException e) {
            log.error("no users found: " + e.getMessage());
            responseJson.put("status", "no users found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/avatar/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String setAvatar(@PathVariable("id") Long id, HttpServletRequest request) {
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
                String avatar = receivedDataJson.getString("avatar");

                User user = userService.findUserById(id);

                userService.updateAvatar(user, avatar.getBytes());

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (JSONException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (IOException e) {
            log.error("incorrect byte sequence: " + e.getMessage());
            responseJson.put("status", "incorrect byte sequence");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/round/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String setRound(@PathVariable("id") Long id, HttpServletRequest request) {
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
                String round = receivedDataJson.getString("round");

                User user = userService.findUserById(id);

                userService.updateRound(user, round.getBytes());

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (JSONException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (IOException e) {
            log.error("incorrect byte sequence: " + e.getMessage());
            responseJson.put("status", "incorrect byte sequence");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/user/online/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String setOnline(@PathVariable("id") Long userId, HttpServletRequest request) {
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
                Boolean state = receivedDataJson.getBoolean("state");

                User user = userService.findUserById(userId);

                log.info("set online");

                responseJson.put("status", "success");
            } else {
                log.info("user not authorized");
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            log.error("incorrect request headers: " + e.getMessage());
            responseJson.put("status", "incorrect request headers");
        } catch (JSONException e) {
            log.error("incorrect request body: " + e.getMessage());
            responseJson.put("status", "incorrect request body");
        } catch (IOException e) {
            log.error("incorrect byte sequence: " + e.getMessage());
            responseJson.put("status", "incorrect byte sequence");
        } catch (UsernameNotFoundException e) {
            log.error("user not found: " + e.getMessage());
            responseJson.put("status", "user not found");
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @RequestMapping(value = "/user/ping/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String ping(@PathVariable("id") Long userId, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                throw new MissingRequestHeaderException("Authorization", null);
            }
            String jwt = header.substring(7);

            if (JwtProvider.validateToken(jwt)) {
                User user = userService.findUserById(userId);

                userService.updateLastOnline(user, LocalDateTime.now());

                responseJson.put("status", "success");
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
        } catch (Exception e) {
            log.error("unknown error: " + e.getMessage());
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }
}