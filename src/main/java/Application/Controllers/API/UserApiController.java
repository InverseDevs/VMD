package Application.Controllers.API;

import Application.Controllers.API.Exceptions.IdChangeAttemptException;
import Application.Controllers.API.Exceptions.NoUserFoundException;
import Application.Controllers.API.Exceptions.WrongRequestException;
import Application.Database.UserInfoRepository;
import Application.Entities.User.UserInfo;
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
    UserInfoRepository infoRepository;

    @NoArgsConstructor
    @Getter
    private static class InfoWrapper {
        private Long id;
        private String username;
        private String name;
        private String birthTown;
        private long birthDate;
        private String _href;

        public InfoWrapper(UserInfo info) {
            this.id = info.getUserId();
            this.username = info.getUsername();
            this.name = info.getName();
            this.birthTown = info.getBirthTown();
            this.birthDate = info.getBirthDate() != null ? info.getBirthDate().getTime() : 0;
            this._href = Starter.homeLink + Starter.apiLink + userApiLink + "/" + info.getUserId();
        }

        public UserInfo toUserInfo() {
            return new UserInfo(id, username, name, birthTown, new Date(birthDate));
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
        return infoRepository.findAll().stream().map(InfoWrapper::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InfoWrapper one(@PathVariable long id) {
        Optional<UserInfo> infoOptional = infoRepository.findById(id);
        if(infoOptional.isPresent()) {
            return new InfoWrapper(infoOptional.get());
        }
        throw new NoUserFoundException();
    }

    @PatchMapping
    public InfoWrapper patchOne(@RequestBody InfoWrapper wrapper) {
        if(wrapper.id == null) throw new WrongRequestException();
        UserInfo info = infoRepository.findById(wrapper.id).orElseThrow(RuntimeException::new);
        if(wrapper.username != null) throw new IdChangeAttemptException();

        // TODO костыльный мерж переделать!!!!!!!!!!!!!!!
        if(wrapper.name != null) info.setName(wrapper.name);
        if(wrapper.birthTown != null) info.setBirthTown(wrapper.birthTown);
        if(wrapper.birthDate >= 0) info.setBirthDate(new Date(wrapper.birthDate));

        return new InfoWrapper(infoRepository.save(info));
    }
}