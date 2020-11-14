package Application.Database;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

public interface WallPostRepository extends JpaRepository<WallPost, Long> {
    @Query("SELECT p from WallPost p where p.pageType = :pageType and p.pageId = :pageId")
    Iterable<WallPost> findByPage(@Param("pageId") Long pageId, @Param("pageType") WallPost.PageType pageType);

    List<WallPost> findByPageId(Long pageId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes (post_id, user_id) VALUES (:post, :user)", nativeQuery = true)
    int addLike(@Param("post") Long postId, @Param("user") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM likes WHERE post_id = :post AND user_id = :user", nativeQuery = true)
    int removeLike(@Param("post") Long postId, @Param("user") Long userId);

    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM likes WHERE post_id = :post AND user_id = :user", nativeQuery = true)
    Collection<Integer> checkLike(@Param("post") Long postId, @Param("user") Long userId);
}