package Application.Database;

import Application.Entities.Content.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findById(Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes_comments (comment_id, user_id) VALUES (:comment, :user)", nativeQuery = true)
    int addLike(@Param("comment") Long commentId, @Param("user") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes_comments WHERE comment_id = :comment AND user_id = :user", nativeQuery = true)
    int removeLike(@Param("comment") Long commentId, @Param("user") Long userId);

    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM likes_comments WHERE comment_id = :comment AND user_id = :user", nativeQuery = true)
    Collection<Integer> checkLike(@Param("comment") Long commentId, @Param("user") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE comments SET picture = :picture where id = :id", nativeQuery = true)
    int updatePicture(@Param("id") Long commentId, @Param("picture") byte[] picture);
}
