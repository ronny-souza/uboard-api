package br.com.uboard.configuration;

import br.com.uboard.core.model.properties.ApplicationConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketConfigurationTest {

    private WebSocketConfiguration webSocketConfiguration;

    @Mock
    private ApplicationConfigurationProperties applicationConfigurationProperties;

    @BeforeEach
    void init() {
        this.webSocketConfiguration = new WebSocketConfiguration(this.applicationConfigurationProperties);
    }

    @Test
    @DisplayName("Should configure the endpoints in StompEndpoints instance")
    void shouldConfigureTheEndpointsStompEndpointsInstance() {
        StompEndpointRegistry stompEndpointRegistryAsMock = mock(StompEndpointRegistry.class);
        StompWebSocketEndpointRegistration stompWebSocketEndpointRegistrationAsMock = mock(StompWebSocketEndpointRegistration.class);

        when(stompEndpointRegistryAsMock.addEndpoint(anyString())).thenReturn(stompWebSocketEndpointRegistrationAsMock);
        when(this.applicationConfigurationProperties.getAllowedOrigins()).thenReturn(List.of("http://localhost:4200"));

        this.webSocketConfiguration.registerStompEndpoints(stompEndpointRegistryAsMock);

        ArgumentCaptor<String> endpointArgumentCaptor = forClass(String.class);

        verify(stompEndpointRegistryAsMock).addEndpoint(endpointArgumentCaptor.capture());
        assertEquals("/ws", endpointArgumentCaptor.getValue());
    }

    @Test
    @DisplayName("Should configure the WebSocket message broker")
    void shouldConfigureWebSocketMessageBroker() {
        MessageBrokerRegistry messageBrokerRegistryAsMock = mock(MessageBrokerRegistry.class);

        this.webSocketConfiguration.configureMessageBroker(messageBrokerRegistryAsMock);

        ArgumentCaptor<String> destinationPrefixArgumentCaptor = forClass(String.class);
        ArgumentCaptor<String> prefixArgumentCaptor = forClass(String.class);

        verify(messageBrokerRegistryAsMock).enableSimpleBroker(destinationPrefixArgumentCaptor.capture());
        verify(messageBrokerRegistryAsMock).setApplicationDestinationPrefixes(prefixArgumentCaptor.capture());

        assertEquals("/poker-room", destinationPrefixArgumentCaptor.getValue());
        assertEquals("/app", prefixArgumentCaptor.getValue());
    }
}
