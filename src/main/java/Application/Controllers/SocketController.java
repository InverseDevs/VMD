package Application.Controllers;

import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.MessageAdapter;
import Application.Services.ChatService;
import Application.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Controller
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
public class SocketController {
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;

    @MessageMapping("/user-all")
    @SendTo("/topic/user")
    public String send(@Payload MessageAdapter messageAdapter) {
        ChatMessage receivedMessage = new ChatMessage();
        receivedMessage.setChat(chatService.getChatById(Long.valueOf(messageAdapter.getChat_id())));
        receivedMessage.setSender(userService.findUserById(Long.valueOf(messageAdapter.getSender_id())));
        receivedMessage.setContent(messageAdapter.getMessage());
        receivedMessage.setSentTime(LocalDateTime.now());

        ChatMessage savedMessage = chatService.saveMessage(receivedMessage);

        return savedMessage.toJson().toString();
    }
}