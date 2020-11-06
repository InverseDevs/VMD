package Application.Controllers.API;

import Application.Controllers.API.Exceptions.IdChangeAttemptException;
import Application.Controllers.API.Exceptions.NoUserFoundException;
import Application.Controllers.API.Exceptions.WrongRequestException;
import Application.Database.User.UserRepository;
import Application.Entities.User.User;
import Application.Starter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                    "_href='" + _href + "\n'}";
        }
    }

    @GetMapping
    public List<InfoWrapper> all() {
        return repository.findAll().stream().map(InfoWrapper::new).collect(Collectors.toList());
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