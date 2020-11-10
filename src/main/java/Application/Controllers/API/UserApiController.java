package Application.Controllers.API;

import Application.Controllers.API.Exceptions.IdChangeAttemptException;
import Application.Controllers.API.Exceptions.NoUserFoundException;
import Application.Controllers.API.Exceptions.WrongRequestException;
import Application.Database.User.UserRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Starter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Starter.apiLink + UserApiController.userApiLink)
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
        private long birthDate;
        private String _href;

        public InfoWrapper(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.birthTown = user.getBirthTown();
            this.birthDate = user.getBirthDate() != null ? user.getBirthDate().getTime() : 0;
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
    public String all(HttpServletRequest request) {
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
                responseJson.put("status", "user not authorized");
            }
        } catch (MissingRequestHeaderException e) {
            responseJson.put("status", "incorrect request headers");
        } catch (UsernameNotFoundException e) {
            responseJson.put("status", "no users found");
        } catch (Exception e) {
            responseJson.put("status", "unknown error");
        }

        return responseJson.toString();
    }

    @GetMapping("/{id}")
    public InfoWrapper one(@PathVariable long id) {
        Optional<User> userOptional = repository.findById(id);
        if(userOptional.isPresent()) {
            return new InfoWrapper(userOptional.get());
        }
        throw new NoUserFoundException();
    }

    @PatchMapping
    public InfoWrapper patchOne(@RequestBody InfoWrapper wrapper) {
        if(wrapper.id == null) throw new WrongRequestException();
        User user = repository.findById(wrapper.id).orElseThrow(NoUserFoundException::new);
        if(wrapper.username != null) throw new IdChangeAttemptException();

        // TODO костыльный мерж переделать!!!!!!!!!!!!!!!
        if(wrapper.name != null) user.setName(wrapper.name);
        if(wrapper.birthTown != null) user.setBirthTown(wrapper.birthTown);
        if(wrapper.birthDate >= 0) user.setBirthDate(new Date(wrapper.birthDate));

        return new InfoWrapper(repository.save(user));
    }
}