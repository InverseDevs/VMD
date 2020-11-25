package Application.Controllers;

import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization")
public class SocketController {

    @MessageMapping("/user-all")
    @SendTo("/topic/user")
    public String send(@Payload MessageAdapter messageAdapter) {
        ChatMessage message = new ChatMessage();
        //message.setChat(messageAdapter.chat_id);
        //message.setSender(messageAdapter.sender_id);

        message.setContent(messageAdapter.getMessage());

        return message.toJson().toString();
    }
}