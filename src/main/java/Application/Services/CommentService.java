package Application.Services;

import Application.Exceptions.Comment.CommentNotFoundException;
import Application.Database.CommentRepository;
import Application.Entities.Content.Comment;
import Application.Entities.User;
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

    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        return comment;
    }

    public void addComment(Comment comment) {
        comment.setSentTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (!commentOptional.isPresent()) {
            throw new CommentNotFoundException("comment not found");
        }
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
