package Application.Services;

import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Database.User.UserRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Entities.Content.WallPost;
import Application.Database.WallPostRepository;
import Application.Entities.User;
import Application.Entities.Wall.Wall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@Deprecated
public class WallPostService {
    @Autowired
    private WallPostRepository postRepository;
    @Autowired
    private UserWallRepository userWallRepository;
    @Autowired
    UserRepository userRepository;

    public Iterable<WallPost> allPosts() { return postRepository.findAll(); }

    @Deprecated
    public Iterable<WallPost> findAllUserPagePosts(Long userId) throws WallPostNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) return null;
        else return new ArrayList<>(userWallRepository.findByUser(userOptional.get()).getPosts());
    }

    public WallPost postById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void addPost(WallPost post) {
        post.setSentTime(new Date());
        postRepository.save(post);
    }

    public void addPost(String message, User sender, Wall wall) {
        WallPost post = new WallPost(sender, message, new Date(), wall);
        postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
