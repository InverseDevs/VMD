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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    public List<User> allUsers() {
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

    public void deleteFriend(User user, User friend) {
        userRepository.deleteFriend(user, friend.getId());
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

    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}