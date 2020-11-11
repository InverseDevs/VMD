package Application.Services;

import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Entities.Wall.UserWall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserWallRepository wallRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserByToken(String token) throws UsernameNotFoundException {
        User user = userRepository.findByToken(token);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(new User());
    }

    public User findUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public Iterable<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setWall(wallRepository.save(new UserWall(user)));
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setToken(generateToken());
        userRepository.save(user);
        return true;
    }

    public User createUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) return null;
        this.makeUser(user);
        user.setToken(generateToken());
        userRepository.save(user);
        user.setWall(wallRepository.save(new UserWall(user)));
        return userRepository.save(user);
    }

    public void permitUser(String token) {
        userRepository.permitUser(token);
    }

    public void makeAdmin(User user) {
        userRepository.makeAdmin(user);
    }

    public void makeUser(User user) {
        userRepository.makeUser(user);
    }

    public void addFriend(User user, User friend) {
        userRepository.addFriend(user, friend.getId());
    }

    public boolean friendExists(User user, User friend) {
        return userRepository.checkFriend(user, friend);
    }

    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}