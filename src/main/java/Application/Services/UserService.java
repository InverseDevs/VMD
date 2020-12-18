package Application.Services;

import Application.Cache.UserCache;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Exceptions.User.Exist.UserAlreadyExists;
import Application.Exceptions.User.Exist.UserAlreadyExistsByEmail;
import Application.Exceptions.User.Exist.UserAlreadyExistsByUsername;
import Application.Exceptions.User.NoUserFoundException;
import Application.Exceptions.User.UserIsNotPersistedException;
import Application.Starter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserWallRepository wallRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role getUserRole() {
        return roleRepository.findById(Starter.userRoleId).get();
    }

    private Role getAdminRole() {
        return roleRepository.findById(Starter.adminRoleId).get();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Создает пользователя с указанным набором данных.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @param email    электронная почта пользователя.
     * @return объект, соответствующий пользователю.
     * @throws UserAlreadyExists пользователь с такими данными уже существует.
     */
    public User createUser(String username, String password, String email) throws UserAlreadyExists {
        return createUser(new User(username, password, email));
    }

    /**
     * Создает пользователя с указанным набором данных.
     *
     * @param username
     * @param password
     * @param email
     * @param name
     * @param birthTown
     * @param birthDate
     * @return объект, соответствующий пользователю.
     * @throws UserAlreadyExists пользователь с такими данными уже существует.
     */
    public User createUser(String username, String password, String email, String name,
                           String birthTown, LocalDate birthDate) throws UserAlreadyExists {
        return createUser(new User(username, password, email, name, birthTown, birthDate));
    }

    private User createUser(User user) throws UserAlreadyExists {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new UserAlreadyExistsByUsername();
        if (userRepository.existsByEmail(user.getEmail()))
            throw new UserAlreadyExistsByEmail();
        user.setRoles(Collections.singleton(this.getUserRole()));
        wallRepository.save(user.getWall());
        return userRepository.save(user);
    }

    public User findUserById(Long id) throws NoUserFoundException {
        User userFromCache = UserCache.getUser(id);
        log.info("get");
        if (userFromCache == null) {
            User user = userRepository.findById(id).orElseThrow(NoUserFoundException::new);
            UserCache.cacheUser(user);
            log.info("cache");
            return user;
        } else {
            log.info("return");
            return userFromCache;
        }
    }

    public User findUserByEmail(String email) throws NoUserFoundException {
        return userRepository.findByEmail(email).orElseThrow(NoUserFoundException::new);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public void permitUser(Long id) {
        userRepository.permitUser(id);
    }

    public void permitUser(User user) {
        if (user.getId() == null) throw new UserIsNotPersistedException();
        user.setPermitted(true);
        userRepository.permitUser(user.getId());
    }

    public void makeAdmin(User user) {
        userRepository.makeAdmin(user);
    }

    public void makeUser(User user) {
        userRepository.makeUser(user);
    }

    public void makeFriends(User user1, User user2) {
        userRepository.addFriend(user1, user2);
        userRepository.addFriend(user2, user1);
        userRepository.deleteFriendRequest(user1, user2);
        userRepository.deleteFriendRequest(user2, user1);
    }

    public void addFriendRequest(User user, User friend) {
        userRepository.addFriendRequest(user, friend);
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

    public void updateLastOnline(User user, LocalDateTime lastOnline) {
        userRepository.updateLastOnline(user, lastOnline);
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