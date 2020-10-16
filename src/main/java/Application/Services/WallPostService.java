package Application.Services;

import Application.Content.WallPost;
import Application.Database.WallPostRepository;
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

    public WallPost postById(Long id) { return repository.findById(id); }

    public void addPost(WallPost post) {
        post.setSentTime(new Date());
        repository.save(post);
    }

    public void addPost(String message, String sender, WallPost.PageType pageType, Long pageId) {
        WallPost post = new WallPost();
        post.setSentTime(new Date());
        post.setSender(sender);
        post.setContent(message);
        post.setPageType(pageType);
        post.setPageId(pageId);
        repository.save(post);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
