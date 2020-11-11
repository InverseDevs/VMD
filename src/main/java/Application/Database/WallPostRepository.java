package Application.Database;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Entities.Wall.Wall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WallPostRepository extends JpaRepository<WallPost, Long> {
    List<WallPost> findByWall(Wall wall);
    List<WallPost> findBySender(User sender);
}