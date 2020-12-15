package Application.Database.User;

import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Transactional
public class CustomUsersImpl implements CustomUsers {
    @PersistenceContext
    private EntityManager em;

    private User findInDb(User user) {
        return em.find(User.class, user.getId());
    }

    private User findInDb(Long userId) {
        return em.find(User.class, userId);
    }


    @Override
    public User addFriend(User user, User friend) {
        User userInDb = findInDb(user);
        User friendInDb = findInDb(friend);
        userInDb.getFriends().add(friendInDb);
        return userInDb;
    }

    @Override
    public User addFriendRequest(User user, User friend) {
        User userInDb = findInDb(user);
        User friendInDb = findInDb(friend);
        friendInDb.getFriendRequests().add(user);
        return friendInDb;
    }

    @Override
    public User deleteFriend(User user, User friend) {
        User userInDb = findInDb(user);
        User friendInDb = findInDb(friend);
        userInDb.getFriends().remove(friendInDb);
        return userInDb;
    }

    @Override
    public User deleteFriendRequest(User user, User friend) {
        User userInDb = findInDb(user);
        User friendInDb = findInDb(friend);
        friendInDb.getFriendRequests().remove(user);
        return friendInDb;
    }

    @Override
    public User addFriend(User user, Long friendId) {
        User userInDb = findInDb(user);
        User friend = findInDb(friendId);
        userInDb.getFriends().add(friend);
        return userInDb;
    }

    @Override
    public User addFriendRequest(User user, Long friendId) {
        User friend = findInDb(friendId);
        friend.getFriendRequests().add(user);
        return friend;
    }

    @Override
    public User deleteFriendRequest(User user, Long friendId) {
        User userInDb = findInDb(user);
        User friend = findInDb(friendId);
        userInDb.getFriendRequests().remove(friend);
        return userInDb;
    }

    @Override
    public boolean checkFriend(User user, User friend) {
        User friendInDb = findInDb(user);
        User userInDb = findInDb(friend);
        return userInDb.getFriends().contains(friendInDb);
    }

    @Override
    public User deleteFriend(User user, Long friendId) {
        User userInDb = findInDb(user);
        User friend = findInDb(friendId);
        userInDb.getFriends().remove(friend);
        return userInDb;
    }

    @Override
    public User makeAdmin(User user) {
        User userInDb = findInDb(user);
        Role adminRole = em.find(Role.class, 2L);
        userInDb.setRoles(Collections.singleton(adminRole));
        return userInDb;
    }

    @Override
    public User updateAvatar(User user, byte[] avatar) {
        User userInDb = findInDb(user);
        userInDb.setAvatar(avatar);
        return userInDb;
    }

    @Override
    public User updateRound(User user, byte[] round) {
        User userInDb = findInDb(user);
        userInDb.setRound(round);
        return userInDb;
    }

    @Override
    public User updateUsername(User user, String username) {
        User userInDb = findInDb(user);
        userInDb.setUsername(username);
        return userInDb;
    }

    @Override
    public User updateName(User user, String name) {
        User userInDb = findInDb(user);
        userInDb.setName(name);
        return userInDb;
    }

    @Override
    public User updateBirthTown(User user, String birthTown) {
        User userInDb = findInDb(user);
        userInDb.setBirthTown(birthTown);
        return userInDb;
    }

    @Override
    public User updateStudyPlace(User user, String studyPlace) {
        User userInDb = findInDb(user);
        userInDb.setStudyPlace(studyPlace);
        return userInDb;
    }

    @Override
    public User updateBirthDate(User user, LocalDate birthDate) {
        User userInDb = findInDb(user);
        userInDb.setBirthDate(birthDate);
        return userInDb;
    }

    @Override
    public User updateLanguages(User user, String languages) {
        User userInDb = findInDb(user);
        userInDb.setLanguages(languages);
        return userInDb;
    }

    @Override
    public User updatePhone(User user, String phone) {
        User userInDb = findInDb(user);
        userInDb.setPhone(phone);
        return userInDb;
    }

    @Override
    public User updateHobbies(User user, String hobbies) {
        User userInDb = findInDb(user);
        userInDb.setHobbies(hobbies);
        return userInDb;
    }

    @Override
    public User updateLastOnline(User user, LocalDateTime lastOnline) {
        User userInDb = findInDb(user);
        userInDb.setLastOnline(lastOnline);
        return userInDb;
    }

    @Override
    public User makeUser(User user) {
        User userInDb = findInDb(user);
        Role userRole = em.find(Role.class, 1L);
        userInDb.setRoles(Collections.singleton(userRole));
        return userInDb;
    }
}
