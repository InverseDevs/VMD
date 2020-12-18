package Application.Cache;

import Application.Entities.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class UserCache {
    private static final Cache<Long, User> userCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static User getUser(Long userId) {
        return userCache.getIfPresent(userId);
    }

    public static void cacheUser(User user) {
        User u = new User();
        u.setRoles(user.getRoles());

        userCache.put(user.getId(), u);
    }
}
