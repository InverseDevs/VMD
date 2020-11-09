package Application.Database.User;

import Application.Entities.User;

public interface CustomUsers {
    User addFriend(User user, Long friendId);
    boolean checkFriend(User user, User friend);
    User deleteFriend(User user, Long friendId);
    User makeAdmin(User user);
    User makeUser(User user);
    //User updateAvatar(User user, byte[] avatar);
}
