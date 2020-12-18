package Application.Cache;

import Application.Entities.Role;
import Application.Entities.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserCache {
    private static final Cache<Long, UserAdapter> userCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static User getUser(Long userId) {
        UserAdapter u = userCache.getIfPresent(userId);
        User user = new User();
        user.setRoles(u.roles);

        return user;
    }

    public static void cacheUser(User user) {
        UserAdapter u = new UserAdapter();
        u.roles = user.getRoles();

        userCache.put(user.getId(), u);
    }

    static class UserAdapter {
        private Set<Role> roles;
    }
}
