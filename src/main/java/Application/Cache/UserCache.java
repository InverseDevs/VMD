package Application.Cache;

import Application.Entities.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class UserCache {
    private static final Cache<Long, User> userCache = CacheBuilder.newBuilder().build();

    public static User getUser(Long userId) {
        return userCache.getIfPresent(userId);
    }

    public static void cacheUser(User user) {
        userCache.put(user.getId(), user);
    }
}
