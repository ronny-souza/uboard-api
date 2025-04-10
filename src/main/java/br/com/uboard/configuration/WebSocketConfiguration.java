package br.com.uboard.configuration;

import br.com.uboard.core.model.properties.ApplicationConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final ApplicationConfigurationProperties applicationConfigurationProperties;

    public WebSocketConfiguration(ApplicationConfigurationProperties applicationConfigurationProperties) {
        this.applicationConfigurationProperties = applicationConfigurationProperties;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(this.applicationConfigurationProperties.getAllowedOrigins().toArray(new String[0]));
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/poker-room");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
