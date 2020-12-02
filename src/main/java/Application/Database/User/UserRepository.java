package Application.Database.User;

import Application.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUsers {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> deleteByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u Set u.permitted = true WHERE id = :id")
    void permitUser(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.commentAccess = :access WHERE u = :user")
    void updateCommentAccess(@Param("user") User user, @Param("access") User.Access commentAccess);

    @Modifying
    @Query("UPDATE User u SET u.postAccess = :access WHERE u = :user")
    void updatePostAccess(@Param("user") User user, @Param("access") User.Access postAccess);

    @Modifying
    @Query("UPDATE User u SET u.commentAccess = :access WHERE u = :user")
    void updateMessageAccess(@Param("user") User user, @Param("access") User.Access messageAccess);
}