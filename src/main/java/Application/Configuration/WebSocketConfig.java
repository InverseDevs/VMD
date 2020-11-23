////
////import Application.Handlers.ChatHandler;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.messaging.simp.config.MessageBrokerRegistry;
////import org.springframework.web.socket.config.annotation.*;
////
////@Configuration
////@EnableWebSocket
////@EnableWebSocketMessageBroker
////public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws").withSockJS();
////    }
////
////    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////        registry.addHandler(new ChatHandler(), "/ws");
////    }
////
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry registry) {
////        registry.setApplicationDestinationPrefixes("/app");
////        registry.enableSimpleBroker("/chat/topic");
////    }
////}
//
//package Application.Configuration;
//
//import Application.Handlers.ChatHandler;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new ChatHandler(), "/ws");
//    }
//
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry registry) {
////        registry.setApplicationDestinationPrefixes("/app");
////        registry.enableSimpleBroker("/chat/topic");
////    }
////
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws").withSockJS();
////    }
//}

package Application.Configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/websocket-chat")
                .setAllowedOrigins("https://verymagicduck.netlify.app/websocket-chat")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }
}