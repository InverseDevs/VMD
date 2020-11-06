package Application.Services;

import Application.Entities.Content.WallPost;
import Application.Database.WallPostRepository;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WallPostService {
    @Autowired
    private WallPostRepository repository;

    public Iterable<WallPost> allPosts() { return repository.findAll(); }

    public Iterable<WallPost> allUserpagePosts(Long userId) {
        return repository.findByPage(userId, WallPost.PageType.USER);
    }

    public WallPost postById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void addPost(WallPost post) {
        post.setSentTime(new Date());
        repository.save(post);
    }

    public void addPost(String message, User sender, WallPost.PageType pageType, Long pageId) {
        WallPost post = new WallPost(sender, message, new Date(), pageId, pageType);
        repository.save(post);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
