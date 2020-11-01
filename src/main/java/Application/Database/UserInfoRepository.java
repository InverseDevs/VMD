package Application.Database;

import Application.Content.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
    UserInfo deleteByUsername(String username);
}