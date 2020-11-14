package Application.Controllers.API;

import Application.Controllers.API.Exceptions.IdChangeAttemptException;
import Application.Controllers.API.Exceptions.NoUserFoundException;
import Application.Controllers.API.Exceptions.WrongRequestException;
import Application.Database.User.UserRepository;
import Application.Entities.User;
import Application.Security.JwtProvider;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(Starter.apiLink + UserApiController.userApiLink)
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
public class UserApiController {
    public final static String userApiLink = "/users";

    @Autowired
    UserRepository repository;

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
                List<User> users = repository.findAll();

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
                Optional<User> userOptional = repository.findById(id);

                if (!userOptional.isPresent()) {
                    throw new NoUserFoundException();
                } else {
                    responseJson = userOptional.get().toJson();
                }
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

    @PatchMapping
    public InfoWrapper patchOne(@RequestBody InfoWrapper wrapper) {
        if (wrapper.id == null) throw new WrongRequestException();
        User user = repository.findById(wrapper.id).orElseThrow(NoUserFoundException::new);
        if (wrapper.username != null) throw new IdChangeAttemptException();

        // TODO костыльный мерж переделать!!!!!!!!!!!!!!!
        if (wrapper.name != null) user.setName(wrapper.name);
        if (wrapper.birthTown != null) user.setBirthTown(wrapper.birthTown);
        user.setBirthDate(wrapper.birthDate);

        return new InfoWrapper(repository.save(user));
    }
}