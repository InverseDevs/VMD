package Application.Database.Group;

import Application.Entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, CustomGroup {
    Optional<Group> findByNamedLink(String namedLink);
    boolean existsByNamedLink(String namedLink);

    @Transactional
    @Query(value = "SELECT group_id FROM group_members WHERE user_id = :id", nativeQuery = true)
    List<Long> findGroupsByUserId(@Param("id") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE groups SET picture = :picture where id = :id", nativeQuery = true)
    int updatePicture(@Param("id") Long groupId, @Param("picture") byte[] picture);
}