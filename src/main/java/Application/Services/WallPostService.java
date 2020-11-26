package Application.Services;

import Application.Controllers.API.Exceptions.WallPost.WallPostNotFoundException;
import Application.Database.User.UserRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Database.Wall.WallRepository;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Entities.Wall.Wall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Deprecated
public class WallPostService {
    @Autowired
    private WallPostRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WallRepository wallRepository;
    @Autowired
    private UserWallRepository userWallRepository;

    public Iterable<WallPost> allPosts() {
        Iterable<WallPost> posts = repository.findAll();

        if (posts == null) {
            throw new WallPostNotFoundException("No posts found");
        }

        return posts;
    }

    public Iterable<WallPost> allUserPagePosts(Long userId) throws WallPostNotFoundException {
        Iterable<WallPost> posts = repository.findByWall(userRepository.findById(userId).get().getWall());
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
        post.setSentTime(LocalDateTime.now());
        repository.save(post);
    }

    public void addPost(String message, User sender, WallPost.PageType pageType, Long pageId) {
        Wall wall;
        if(pageType.equals(WallPost.PageType.USER) && userRepository.existsById(pageId)) {
            wall = userRepository.findById(pageId).get().getWall();
        } else if(pageType.equals(WallPost.PageType.GROUP)) {
            // some logic
            wall = null;
        }
        else {
            wall = null;
        }
        WallPost post = new WallPost(sender, message, LocalDateTime.now(), wall);
        repository.save(post);
    }

    public void like(WallPost post, User user) {
        repository.addLike(post.getId(), user.getId());
    }

    public void removeLike(WallPost post, User user) {
        repository.removeLike(post.getId(), user.getId());
    }

    public boolean checkLike(WallPost post, User user) {
        return repository.checkLike(post.getId(), user.getId()).size() == 0;
    }

    public void updatePicture(WallPost post, byte[] picture) {
        repository.updatePicture(post.getId(), picture);
    }

    public void deletePost(Long postId) {
        repository.deleteById(postId);
    }
}
