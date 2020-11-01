package Application.Services;

import Application.Content.UserInfo;
import Application.Database.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {
    private UserInfoRepository repository;

    @Autowired
    public UserInfoService(UserInfoRepository repository) {
        this.repository = repository;
    }

    public UserInfo findUserInfo(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    public UserInfo findUserInfo(String username) {
        return repository.findByUsername(username);
    }

    public void updateUserInfo(UserInfo info) {
        Optional<UserInfo> prevInfo = repository.findById(info.getUserId());
        if(prevInfo.isPresent())
            repository.deleteById(info.getUserId());
        repository.save(info);
    }
}
