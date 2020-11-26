package Application.Database.Chat;

import Application.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE chats SET picture = :picture where id = :id", nativeQuery = true)
    int updatePicture(@Param("id") Long chatId, @Param("picture") byte[] picture);
}
