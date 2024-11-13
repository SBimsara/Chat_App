package com.sithirabimsara.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;

    public Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createNewChatIfNotExist) {
        return chatRoomRepo.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewChatIfNotExist) {
                        String newChatId = createChatRoom(senderId, recipientId);
                        return Optional.of(newChatId);
                    }
                    return Optional.empty();
                });



    }

    public String createChatRoom(Long senderId, Long recipientId) {
        String chatId = generateChatId(senderId, recipientId);
        ChatRoom newSenderRecipientChatRoom = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom newRecipientSenderChatRoom = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepo.save(newSenderRecipientChatRoom);
        chatRoomRepo.save(newRecipientSenderChatRoom);

        return chatId;
    }

    private String generateChatId(Long senderId, Long recipientId) {
        return senderId + "_" + recipientId + "_" + Calendar.getInstance().getTimeInMillis();
    }
}
