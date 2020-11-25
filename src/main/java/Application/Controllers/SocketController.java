package Application.Controllers;

import Application.Entities.Content.ChatMessage;
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

        message.setContent(messageAdapter.message);

        return message.toJson().toString();
    }

    class MessageAdapter {
        public MessageAdapter() {}

        public MessageAdapter(Long chat_id, Long sender_id, String message) {
            this.chat_id = chat_id;
            this.sender_id = sender_id;
            this.message = message;
        }

        Long chat_id;
        Long sender_id;
        String message;
    }
}