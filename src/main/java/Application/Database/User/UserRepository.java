package Application.Database.User;

import Application.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, CustomUsers {
    User findByUsername(String username);
    User findByEmail(String email);
    User deleteByUsername(String username);

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