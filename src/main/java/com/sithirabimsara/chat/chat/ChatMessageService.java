package com.sithirabimsara.chat.chat;

import com.sithirabimsara.chat.chatroom.ChatRoom;
import com.sithirabimsara.chat.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepo chatMessageRepo;
    private final ChatRoomService chatRoomService;

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatMessage.setChatId(String.valueOf(chatId));
        chatMessageRepo.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if((chatId == null) && (chatId.isEmpty())){
            return new ArrayList<>();
        }
        else {
            return chatMessageRepo.findByChatId(chatId);
        }
    }





}
