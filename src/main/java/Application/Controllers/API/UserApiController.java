package Application.Controllers.API;

import Application.Controllers.API.Exceptions.NoUserFoundException;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import Application.Starter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Starter.apiLink + UserApiController.userApiLink)
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
public class UserApiController {
    public final static String userApiLink = "/users";

    @Autowired
    UserService userService;

    @NoArgsConstructor
    @Getter
    private static class InfoWrapper {
        private Long id;
        private String username;
        private String name;
        private String birthTown;
        private LocalDate birthDate;
        private String _href;

        public InfoWrapper(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.birthTown = user.getBirthTown();
            //TODO Убрать этот костыль, а лучше и всю обёртку вообще
            this.birthDate = user.getBirthDate();
            this._href = Starter.homeLink + Starter.apiLink + userApiLink + "/" + user.getId();
        }

        @Override
        public String toString() {
            return "InfoWrapper{" + '\n' +
                    "id=" + id + '\n' +
                    "username='" + username + "'" + '\n' +
                    "name='" + name + "'" + '\n' +
                    "birthTown='" + birthTown + "'" + '\n' +
                    "birthDate=" + birthDate + "" + '\n' +
                    "_href='" + _href +
                    "\n'}";
        }
    }

    @GetMapping
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

                int idx = 0;
                for (User user : users) {
                    responseJson.put("user_" + ++idx, user.toJson());
                }
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

    @GetMapping("/{id}")
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

    @PatchMapping("/{id}")
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
                String newUsername = receivedDataJson.getString("username");
                String newBirthTown = receivedDataJson.getString("birth_town");
                String newStudyPlace = receivedDataJson.getString("study_place");
                LocalDate newBirthDate = LocalDate.parse(receivedDataJson.getString("birth_date"));
                String newLanguages = receivedDataJson.getString("languages");
                String newPhoneNumber = receivedDataJson.getString("phone");
                String newHobbies = receivedDataJson.getString("hobbies");

                User user = userService.findUserById(id);

                userService.updateUsername(user, newUsername);
                userService.updateBirthTown(user, newBirthTown);
                userService.updateStudyPlace(user, newStudyPlace);
                userService.updateBirthDate(user, newBirthDate);
                userService.updateLanguages(user, newLanguages);
                userService.updatePhone(user, newPhoneNumber);
                userService.updateHobbies(user, newHobbies);

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

//    @PatchMapping
//    public InfoWrapper patchOne(@RequestBody InfoWrapper wrapper) {
//        if (wrapper.id == null) throw new WrongRequestException();
//        User user = userService.findById(wrapper.id).orElseThrow(NoUserFoundException::new);
//        if (wrapper.username != null) throw new IdChangeAttemptException();
//
//        // TODO костыльный мерж переделать!!!!!!!!!!!!!!!
//        if (wrapper.name != null) user.setName(wrapper.name);
//        if (wrapper.birthTown != null) user.setBirthTown(wrapper.birthTown);
//        user.setBirthDate(wrapper.birthDate);
//
//        return new InfoWrapper(userService.save(user));
//    }
}