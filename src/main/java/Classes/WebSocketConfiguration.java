package Classes;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer  {
	
	public void configureMessageBroker(MessageBrokerRegistry confi) {
		confi.enableSimpleBroker("/topic");
		confi.setApplicationDestinationPrefixes("/app");
	}

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
		registry.addEndpoint("/news-comment").withSockJS();
		registry.addEndpoint("/contact").withSockJS();
		registry.addEndpoint("/news-likes").withSockJS();
	}

}