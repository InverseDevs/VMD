package Application.Database;

import Application.Entities.User;
import Application.Messages.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ChatRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public ChatRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

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

    public void saveMessage(Long chatId, String sender, String message) {
        jdbc.update("insert into messages (chat_id, sender, message) values (?,?,?)", chatId, sender, message);
    }

    public void deleteMessageById(Long chatId) {
        jdbc.update("delete from messages where id = ?", chatId);
    }

    public List<ChatMessage> getMessages(Long chatId) {
        return jdbc.query("select messages.sender as sender, " +
                        "messages.message as message " +
                        "from messages " +
                        "where messages.chat_id = " + chatId,
                this::mapRowToMessage);
    }

    private ChatMessage mapRowToMessage(ResultSet resultSet, int rowNum) throws SQLException {
        ChatMessage message = new ChatMessage();

        message.setSender(resultSet.getString("sender"));
        message.setContent(resultSet.getString("message"));

        return message;
    }

    private Long mapRowToChat(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("chat_id");
    }
}
