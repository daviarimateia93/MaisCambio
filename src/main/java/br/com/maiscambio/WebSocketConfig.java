package br.com.maiscambio;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import me.gerenciar.util.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
{
	@Override
	public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry)
	{
		messageBrokerRegistry.enableSimpleBroker(Constants.TEXT_EMPTY);
		messageBrokerRegistry.setApplicationDestinationPrefixes(Constants.TEXT_EMPTY);
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry)
	{
		stompEndpointRegistry.addEndpoint("/ws").withSockJS();
	}
}