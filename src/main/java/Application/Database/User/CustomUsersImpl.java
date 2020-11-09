package Application.Database.User;

import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public User addFriend(User user, Long friendId) {
        User userInDb = findInDb(user);
        User friend = findInDb(friendId);
        userInDb.getFriends().add(friend);
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

//    @Override
//    public User updateAvatar(User user, byte[] avatar) {
//        User userInDb = findInDb(user);
//        userInDb.setAvatar(avatar);
//        return userInDb;
//    }

    @Override
    public User makeUser(User user) {
        User userInDb = findInDb(user);
        Role userRole = em.find(Role.class, 1L);
        userInDb.setRoles(Collections.singleton(userRole));
        return userInDb;
    }
}
