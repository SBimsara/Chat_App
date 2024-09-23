package com.sithirabimsara.chat.config;

import com.sithirabimsara.chat.chat.ChatMessage;
import com.sithirabimsara.chat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            log.info("User Disconnected : {}" , username);

            // notify the public about the disconnection
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }

        // Handle private chat disconnection
        // Assuming you have some way to track active private chats
        String recipient = (String) headerAccessor.getSessionAttributes().get("recipient"); // Example tracking
        if (recipient != null) {
            var privateChatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSendToUser(recipient, "/queue/private", privateChatMessage);
        }

        // Handle group chat disconnection
        // Assuming you have some way to track which group the user was in
        String groupName = (String) headerAccessor.getSessionAttributes().get("groupName"); // Example tracking
        if (groupName != null) {
            var groupChatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .groupName(groupName)
                    .build();
            messageTemplate.convertAndSend("/topic/group/" + groupName, groupChatMessage);
        }
    }
}

