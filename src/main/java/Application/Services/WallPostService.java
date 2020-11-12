package Application.Services;

import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class WallPostService {
    @Autowired
    private WallPostRepository repository;

    public Iterable<WallPost> allPosts() {
        Iterable<WallPost> posts = repository.findAll();

        if (posts == null) {
            throw new WallPostNotFoundException("No posts found");
        }

        return posts;
    }

    public Iterable<WallPost> allUserPagePosts(Long userId) throws WallPostNotFoundException {
        Iterable<WallPost> posts = repository.findByPage(userId, WallPost.PageType.USER);

        if (posts == null) {
            throw new WallPostNotFoundException("No posts found");
        }

        return posts;
    }

    public WallPost postById(Long id) {
        WallPost post = repository.findById(id).orElse(null);

        if (post == null) {
            throw new WallPostNotFoundException("Post not found");
        }

        return post;
    }

    public void addPost(WallPost post) {
        post.setSentTime(new Date());
        repository.save(post);
    }

    public void addPost(String message, User sender, WallPost.PageType pageType, Long pageId) {
        WallPost post = new WallPost(sender, message, new Date(), pageId, pageType);
        repository.save(post);
    }

    public void like(WallPost post, User user) {
        repository.addLike(post.getId(), user.getId());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
