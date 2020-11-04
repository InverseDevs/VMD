package Application.Database.User;

import Application.Entities.User.User;

public interface CustomUsers {
    void addFriend(User user, Long friendId);
    boolean checkFriend(User user, User friend);
    void deleteFriend(User user, Long friendId);
    void makeAdmin(User user);
    void makeUser(User user);
}
