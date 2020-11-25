package Application.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
    public String send(@Payload JSONObject message) {
        String name = message.getString("name");
        String msg = message.getString("message");

        log.info("message received " + name + " " + msg);
        return message.toString();
    }
}