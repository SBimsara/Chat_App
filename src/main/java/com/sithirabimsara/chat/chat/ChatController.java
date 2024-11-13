package com.sithirabimsara.chat.chat;

import com.sithirabimsara.chat.user.User;
import com.sithirabimsara.chat.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor

public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage= chatMessageService.saveChatMessage(chatMessage);
        User recipient = userService.getUser(chatMessage.getRecipientId());
        messagingTemplate.convertAndSendToUser(recipient.getNickName(), "/queue/messages",
                ChatNotification.builder()
                        .id(savedMessage.getId())
                        .senderId(savedMessage.getSenderId())
                        .recipientId(savedMessage.getRecipientId())
                        .content(savedMessage.getContent())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable("senderId") Long senderId, @PathVariable("recipientId") Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    // Handle private messages
//    @MessageMapping("/chat.sendPrivateMessage")
//    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
//        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/private", chatMessage);
//    }
//
//    // Handle group chat messages
//    @MessageMapping("/chat.sendGroupMessage")
//    public void sendGroupMessage(@Payload ChatMessage chatMessage) {
//        messagingTemplate.convertAndSend("/topic/group/" + chatMessage.getGroupName(), chatMessage);
//    }
}
