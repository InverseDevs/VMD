package Application.Services;

import Application.Database.User.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
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

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.get();
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

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
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

    public void addFriend(User user, User friend) {
        userRepository.addFriend(user, friend.getId());
    }

    public boolean friendExists(User user, User friend) {
        return userRepository.checkFriend(user, friend);
    }

    public void updateAvatar(User user, byte[] avatar) {
        userRepository.updateAvatar(user, avatar);
    }

    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}