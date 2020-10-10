package Application.Services;

import Application.Database.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserByToken(String token) {
        User user = userRepository.findByToken(token);

        return user != null ? user : new User();
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId);

        return user != null ? user : new User();
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return user != null ? user : new User();
    }

    public Iterable<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setToken(generateToken());
        userRepository.save(user);
        return true;
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

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId) != null) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}