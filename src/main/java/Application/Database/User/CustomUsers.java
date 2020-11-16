package Application.Database.User;

import Application.Entities.User;

import java.time.LocalDate;

public interface CustomUsers {
    User addFriend(User user, Long friendId);
    boolean checkFriend(User user, User friend);
    User deleteFriend(User user, Long friendId);
    User makeAdmin(User user);
    User makeUser(User user);
    User updateAvatar(User user, byte[] avatar);

    User updateUsername(User user, String username);
    User updateName(User user, String name);
    User updateBirthTown(User user, String  birthTown);
    User updateStudyPlace(User user, String  studyPlace);
    User updateBirthDate(User user, LocalDate birthDate);
    User updateLanguages(User user, String languages);
    User updatePhone(User user, String phone);
    User updateHobbies(User user, String hobbies);
}
