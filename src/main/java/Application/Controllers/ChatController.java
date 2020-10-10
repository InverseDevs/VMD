package Application.Controllers;

import Application.Entities.User;
import Application.Content.ChatMessage;
import Application.Services.ChatService;
import Application.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatController {

    @Autowired
    ChatService chatService;
    @Autowired
    UserService userService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chat/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        User sender = (User) userService.loadUserByUsername(chatMessage.getSender());
        User receiver = (User) userService.loadUserByUsername(chatMessage.getReceiver());

        long chatId = chatService.getChat(sender.getId(), receiver.getId());
        chatMessage.setChatId(chatId);

        chatService.saveMessage(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/chat/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("sender", chatMessage.getSender());
        return chatMessage;
    }

    @GetMapping("/chat/{token}")
    public String getChat(@PathVariable("token") String token, Model model) {
        User sender = (User) userService.loadUserByUsername(
                String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        User receiver = userService.findUserByToken(token);

        ChatMessage message = new ChatMessage();
        message.setSender(sender.getUsername());
        message.setReceiver(receiver.getUsername());

        long chatId = chatService.getChat(sender.getId(), receiver.getId());

        model.addAttribute("sender", sender.getUsername());
        model.addAttribute("message", message);
        model.addAttribute("messages", chatService.getMessages(chatId).toArray());

        return "chat";
    }
}