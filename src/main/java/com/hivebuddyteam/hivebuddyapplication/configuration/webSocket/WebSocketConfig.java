package com.hivebuddyteam.hivebuddyapplication.configuration.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/specific");
        registry.setApplicationDestinationPrefixes("/app"); // endpoint <--
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-message");
        registry.addEndpoint("/ws-message").withSockJS();
    }
}





//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(dataHandler(), "/dataHandler"); // .setAllowedOrigins("http://example.com")
//    }                                                               // --> add to set sources allowed to call Websocket
//
//    @Bean
//    public WebSocketHandler dataHandler() {
//        return new DataHandler();
//    }


//}
