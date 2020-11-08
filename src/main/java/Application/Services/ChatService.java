package Application.Services;

import Application.Controllers.API.Exceptions.MessageNotFoundException;
import Application.Database.ChatRepository;
import Application.Entities.Content.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public void saveMessage(ChatMessage message) {
        chatRepository.saveMessage(message.getChatId(), message.getSender(), message.getContent());
    }

    public List<ChatMessage> getMessages(Long chatId) {
        List<ChatMessage> messages = chatRepository.getMessages(chatId);

        if (messages == null) {
            throw new MessageNotFoundException("Messages not found");
        }

        return messages;
    }

    public Long getChat(Long senderId, Long receiverId) {
        Long chatId;
        try {
            chatId = chatRepository.getChat(senderId, receiverId);
        } catch (EmptyResultDataAccessException e) {
            chatRepository.createChat(senderId, receiverId);
            chatId = chatRepository.getChat(senderId, receiverId);
        }
        return chatId;
    }
}
