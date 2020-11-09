package Application.Database.User;

import Application.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, CustomUsers {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByToken(String token);
    User deleteByUsername(String username);

    @Modifying
    @Query("UPDATE User u Set u.permitted = true WHERE token = :token")
    void permitUser(@Param("token") String token);
}