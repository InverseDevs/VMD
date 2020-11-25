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
        ChatMessage message = new ChatMessage();
        message.setChat(chatService.getChatById(Long.valueOf(messageAdapter.getChat_id())));
        message.setSender(userService.findUserById(Long.valueOf(messageAdapter.getSender_id())));
        message.setContent(messageAdapter.getMessage());

        return message.toJson().toString();
    }
}