package Application.Services;

import Application.Database.Wall.WallRepository;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.Group;
import Application.Entities.Wall.GroupWall;
import Application.Entities.Wall.UserWall;
import Application.Entities.Wall.Wall;
import Application.Exceptions.Comment.CommentNotFoundException;
import Application.Database.CommentRepository;
import Application.Entities.Content.Comment;
import Application.Entities.User;
import Application.Exceptions.NotEnoughPermissionsException;
import Application.Exceptions.WallPost.WallPostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    WallPostRepository postRepository;

    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        return comment;
    }

    public Comment addComment(User sender, String content, WallPost post)
            throws WallPostNotFoundException, NotEnoughPermissionsException {
        return addComment(sender, content, post, null);
    }

    public Comment addComment(User sender, String content, WallPost post, Comment referenceComment)
            throws WallPostNotFoundException, NotEnoughPermissionsException {
        WallPost updatedPost = postRepository.findById(post.getId()).orElseThrow(WallPostNotFoundException::new);
        if(!updatedPost.getWall().canComment(sender))
            throw new NotEnoughPermissionsException();
        return commentRepository.save(new Comment(sender, content, LocalDateTime.now(), post, referenceComment));
    }

    @Deprecated
    public void addComment(Comment comment) throws NotEnoughPermissionsException {
        comment.setSentTime(LocalDateTime.now());
        if(!comment.getPost().getWall().canComment(comment.getSender()))
            throw new NotEnoughPermissionsException();
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) throws CommentNotFoundException {
        if(!commentRepository.existsById(commentId))
            throw new CommentNotFoundException();
        commentRepository.deleteById(commentId);
    }

    public void deleteCommentByUser(Long commentId, User attempter)
            throws CommentNotFoundException, NotEnoughPermissionsException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if(!comment.getSender().equals(attempter) || !comment.getPost().getWall().canDeleteContent(attempter))
            throw new NotEnoughPermissionsException();
        commentRepository.deleteById(commentId);
    }

    public void like(Comment comment, User user) {
        commentRepository.addLike(comment.getId(), user.getId());
    }

    public void removeLike(Comment comment, User user) {
        commentRepository.removeLike(comment.getId(), user.getId());
    }

    public boolean checkLike(Comment comment, User user) {
        return commentRepository.checkLike(comment.getId(), user.getId()).size() == 0;
    }

    public void updatePicture(Comment comment, byte[] picture) {
        commentRepository.updatePicture(comment.getId(), picture);
    }
}
