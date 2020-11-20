package Application.Database.Wall;

import Application.Entities.Group;
import Application.Entities.Wall.GroupWall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupWallRepository extends JpaRepository<GroupWall, Long> {
    GroupWall findByGroup(Group group);
}
