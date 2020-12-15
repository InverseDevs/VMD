package Application.Database.User;

import Application.Entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CustomUsers {
    @Deprecated
    User addFriend(User user, Long friendId);

    User addFriend(User user, User friend);

    @Deprecated
    User addFriendRequest(User user, Long friendId);

    User addFriendRequest(User user, User friend);

    @Deprecated
    User deleteFriendRequest(User user, Long friendId);

    User deleteFriendRequest(User user, User friend);

    boolean checkFriend(User user, User friend);

    @Deprecated
    User deleteFriend(User user, Long friendId);

    User deleteFriend(User user, User friend);

    User makeAdmin(User user);

    User makeUser(User user);

    User updateAvatar(User user, byte[] avatar);

    User updateRound(User user, byte[] round);

    User updateUsername(User user, String username);

    User updateName(User user, String name);

    User updateBirthTown(User user, String birthTown);

    User updateStudyPlace(User user, String studyPlace);

    User updateBirthDate(User user, LocalDate birthDate);

    User updateLanguages(User user, String languages);

    User updatePhone(User user, String phone);

    User updateHobbies(User user, String hobbies);

    User updateLastOnline(User user, LocalDateTime online);
}
