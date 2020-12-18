package Application.Cache;

import Application.Entities.Role;
import Application.Entities.User;
import Application.Entities.Wall.UserWall;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class UserCache {
    private static final Cache<Long, UserAdapter> userCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static User getUser(Long userId) {
        UserAdapter userAdapter = userCache.getIfPresent(userId);

        if (userAdapter == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userAdapter.id);
            user.setUsername(userAdapter.username);
            user.setPassword(userAdapter.password);
            user.setEmail(userAdapter.email);
            user.setPermitted(userAdapter.permitted);
            user.setName(userAdapter.name);
            user.setBirthTown(userAdapter.birthTown);
            user.setStudyPlace(userAdapter.studyPlace);
            user.setLanguages(userAdapter.languages);
            user.setPhone(userAdapter.phone);
            user.setHobbies(userAdapter.hobbies);
            user.setBirthDate(userAdapter.birthDate);
            user.setLastOnline(userAdapter.lastOnline);
            user.setWall(userAdapter.wall);
            user.setAvatar(userAdapter.avatar);
            user.setRound(userAdapter.round);
            user.setRoles(userAdapter.roles);
            user.setFriends(userAdapter.friends);
            user.setFriendRequests(userAdapter.friendRequests);
            user.setMessageAccess(userAdapter.messageAccess);
            user.setPostAccess(userAdapter.postAccess);
            user.setCommentAccess(userAdapter.commentAccess);

            return user;
        }
    }

    public static void cacheUser(User user) {
        UserAdapter userAdapter = new UserAdapter();
        userAdapter.id = user.getId();
        userAdapter.username = user.getUsername();
        userAdapter.password = user.getPassword();
        userAdapter.email = user.getEmail();
        userAdapter.permitted = user.getPermitted();
        userAdapter.name = user.getName();
        userAdapter.birthTown = user.getBirthTown();
        userAdapter.studyPlace = user.getStudyPlace();
        userAdapter.languages = user.getLanguages();
        userAdapter.phone = user.getPhone();
        userAdapter.hobbies = user.getHobbies();
        userAdapter.birthDate = user.getBirthDate();
        userAdapter.lastOnline = user.getLastOnline();
        userAdapter.wall = user.getWall();
        userAdapter.avatar = user.getAvatar();
        userAdapter.round = user.getRound();
        userAdapter.roles = user.getRoles();
        userAdapter.friends = user.getFriends();
        userAdapter.friendRequests = user.getFriendRequests();
        userAdapter.messageAccess = user.getMessageAccess();
        userAdapter.postAccess = user.getPostAccess();
        userAdapter.commentAccess = user.getCommentAccess();

        userCache.put(user.getId(), userAdapter);
    }

    static class UserAdapter {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Boolean permitted;
        private String name;
        private String birthTown;
        private String studyPlace;
        private String languages;
        private String phone;
        private String hobbies;
        private LocalDate birthDate;
        private LocalDateTime lastOnline;

        private UserWall wall;

        private byte[] avatar;

        private byte[] round;

        private Set<Role> roles;

        private Set<User> friends;

        private Set<User> friendRequests;

        public enum Access {
            EVERYONE, FRIENDS, NOBODY;
        }

        @Enumerated(EnumType.STRING)
        private User.Access messageAccess;
        @Enumerated(EnumType.STRING)
        private User.Access postAccess;
        @Enumerated(EnumType.STRING)
        private User.Access commentAccess;
    }
}
