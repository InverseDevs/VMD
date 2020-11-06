package Application.Database;

import Application.Database.Chat.ChatMessageRepository;
import Application.Entities.User;
import Application.Entities.Content.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ChatRepository {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ChatMessageRepository messageRepository;

    public Long getChat(Long senderId, Long receiverId) throws EmptyResultDataAccessException {
        return jdbc.queryForObject("select " +
                        "chats.id as chat_id " +
                        "from chats " +
                        "where sender_id = " + senderId + " and receiver_id = " + receiverId,
                this::mapRowToChat);
    }

    public void createChat(Long senderId, Long receiverId) {
        jdbc.update("insert into chats (sender_id, receiver_id) values (?,?)", senderId, receiverId);
    }

    public void deleteChat(User sender, User receiver) {
        jdbc.update("delete from chats where sender_id = ? and receiver_id = ?", sender.getId(), receiver.getId());
    }
    public void deleteChatById(Long id) {
        jdbc.update("delete from chats where id = ?", id);
    }

    public void saveMessage(Long chatId, User sender, String message) {
        messageRepository.save(new ChatMessage(sender, message, new Date(), chatId));
    }

    public void deleteMessageById(Long chatId) {
        jdbc.update("delete from messages where id = ?", chatId);
    }

    public List<ChatMessage> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    private Long mapRowToChat(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("chat_id");
    }
}
