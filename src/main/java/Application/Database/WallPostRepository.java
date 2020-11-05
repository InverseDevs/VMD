package Application.Database;

import Application.Entities.Content.WallPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WallPostRepository extends JpaRepository<WallPost, Long> {

    @Query("SELECT p from WallPost p where p.pageType = :pageType and p.pageId = :pageId")
    Iterable<WallPost> findByPage(@Param("pageId") Long pageId, @Param("pageType") WallPost.PageType pageType);
}