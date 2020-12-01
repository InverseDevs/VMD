package Application.Services;

import Application.Database.User.UserRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Exceptions.User.Exist.UserAlreadyExists;
import Application.Exceptions.User.Exist.UserAlreadyExistsByEmail;
import Application.Exceptions.User.Exist.UserAlreadyExistsByUsername;
import Application.Exceptions.User.NoUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserWallRepository wallRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Создает пользователя с указанным набором данных.
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @param email электронная почта пользователя.
     * @return объект, соответствующий пользователю.
     * @throws UserAlreadyExists пользователь с такими данными уже существует.
     */
    public User createUser(String username, String password, String email) throws UserAlreadyExists {
        if(userRepository.existsByUsername(username))
            throw new UserAlreadyExistsByUsername();
        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsByEmail();
        User user = new User(username, password, email);
        wallRepository.save(user.getWall());
        this.makeUser(user);
        return userRepository.save(user);
    }

    public User findUserById(Long id) throws NoUserFoundException {
        return userRepository.findById(id).orElseThrow(NoUserFoundException::new);
    }

    public User findUserByEmail(String email) throws NoUserFoundException {
        return userRepository.findByEmail(email).orElseThrow(NoUserFoundException::new);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    /**
     * Сохраняет пользователя в базу данных при условии, если пользователя с соответсвующим никнеймом не существует.
     *
     * Данный метод является устаревшим и ненадежным. В частности, он не проверяет, существует ли
     * пользователь с данной электронной почтой, поэтому он позволяет создать несколько аккаунтов,
     * зарегистрированных на одну почту.
     *
     * Метод должен быть заменен на {@link UserService#createUser(String, String, String)}
     * @param user пользователь.
     * @return получилось сохранить пользователя в БД или нет.
     * @see UserService#createUser(String, String, String)
     */
    @Deprecated
    public boolean saveUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            return false;

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        wallRepository.save(user.getWall());
        userRepository.save(user);
        return true;
    }

    public void permitUser(Long id) {
        userRepository.permitUser(id);
    }

    public void makeAdmin(User user) {
        userRepository.makeAdmin(user);
    }

    public void makeUser(User user) {
        userRepository.makeUser(user);
    }

    @Deprecated
    public void addFriend(User user, User friend) {
        userRepository.addFriend(user, friend);
    }

    public void makeFriends(User user1, User user2) {
        userRepository.addFriend(user1, user2);
        userRepository.addFriend(user2, user1);
    }

    public void addFriendRequest(User user, User friend) {
        userRepository.addFriendRequest(user, friend);
    }

    @Deprecated
    public void deleteFriend(User user, User friend) {
        userRepository.deleteFriend(user, friend);
    }

    public void deleteFriends(User user1, User user2) {
        userRepository.deleteFriend(user1, user2);
        userRepository.deleteFriend(user2, user1);
    }

    public void deleteFriendRequest(User user, User friend) {
        userRepository.deleteFriendRequest(user, friend);
    }

    public boolean friendExists(User user, User friend) {
        return userRepository.checkFriend(user, friend);
    }

    public void updateAvatar(User user, byte[] avatar) {
        userRepository.updateAvatar(user, avatar);
    }

    public void updateRound(User user, byte[] round) {
        userRepository.updateRound(user, round);
    }

    public void updateUsername(User user, String username) {
        userRepository.updateUsername(user, username);
    }

    public void updateName(User user, String name) {
        userRepository.updateName(user, name);
    }

    public void updateBirthTown(User user, String birthTown) {
        userRepository.updateBirthTown(user, birthTown);
    }

    public void updateStudyPlace(User user, String studyPlace) {
        userRepository.updateStudyPlace(user, studyPlace);
    }

    public void updateBirthDate(User user, LocalDate birthDate) {
        userRepository.updateBirthDate(user, birthDate);
    }

    public void updateLanguages(User user, String languages) {
        userRepository.updateLanguages(user, languages);
    }

    public void updatePhone(User user, String phone) {
        userRepository.updatePhone(user, phone);
    }

    public void updateHobbies(User user, String hobbies) {
        userRepository.updateHobbies(user, hobbies);
    }

    public void updateOnline(User user, Boolean online) {
        userRepository.updateOnline(user, online);
    }

    public void updateCommentAccess(User user, User.Access commentAccess) {
        userRepository.updateCommentAccess(user, commentAccess);
    }

    public void updatePostAccess(User user, User.Access postAccess) {
        userRepository.updatePostAccess(user, postAccess);
    }

    public void updateMessageAccess(User user, User.Access messageAccess) {
        userRepository.updateMessageAccess(user, messageAccess);
    }

    public boolean deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        userRepository.deleteById(userId);
        return exists;
    }
}