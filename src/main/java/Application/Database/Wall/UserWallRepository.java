package Application.Database.Wall;

import Application.Entities.User;
import Application.Entities.Wall.UserWall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWallRepository extends JpaRepository<UserWall, Long> {
    public UserWall findByUser(User user);
}
