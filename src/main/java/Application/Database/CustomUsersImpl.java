package Application.Database;

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

    @Override
    public void addFriend(User user, Long friendId) {
        User userInDb = em.find(User.class, user.getId());
        User friend = em.find(User.class, friendId);
        userInDb.getFriends().add(friend);
    }

    @Override
    public boolean checkFriend(User user, User friend) {
        User friendInDb = em.find(User.class, friend.getId());
        User userInDb = em.find(User.class, user.getId());
        return userInDb.getFriends().contains(friendInDb);
    }

    @Override
    public void deleteFriend(User user, Long friendId) {
        User userInDb = em.find(User.class, user.getId());
        User friend = em.find(User.class, friendId);
        userInDb.getFriends().remove(friend);
    }

    @Override
    public void makeAdmin(User user) {
        User userInDb = em.find(User.class, user.getId());
        Role adminRole = em.find(Role.class, 2L);
        userInDb.setRoles(Collections.singleton(adminRole));
    }

    @Override
    public void makeUser(User user) {
        User userInDb = em.find(User.class, user.getId());
        Role userRole = em.find(Role.class, 1L);
        userInDb.setRoles(Collections.singleton(userRole));
    }
}
