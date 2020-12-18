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
        log.info("cache");
        log.info(user.toString());
        userCache.put(user.getId(), user);
    }
}
