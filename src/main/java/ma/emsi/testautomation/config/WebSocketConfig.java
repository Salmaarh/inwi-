package ma.emsi.testautomation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

    private final TradeWebSocketHandler tradeWebSocketHandler;

    public WebSocketConfig(TradeWebSocketHandler tradeWebSocketHandler) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(tradeWebSocketHandler, "/ws").setAllowedOrigins("*");
        registry.addHandler(tradeWebSocketHandler, "/chat").setAllowedOrigins("*");

    }


}
