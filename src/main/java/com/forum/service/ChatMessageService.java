package com.forum.service;

import com.forum.dto.ChatMessageDto;
import com.forum.dto.ChatMessageSavedDto;
import com.forum.model.ChatMessage;
import com.forum.model.ChatRoom;
import com.forum.model.User;
import com.forum.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getMessagesFromChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findAllByRoom(chatRoom);
    }

    public ChatMessage getLastMessage(ChatRoom chatRoom) {
        return chatMessageRepository.findFirstByRoomOrderByCreatedAtDesc(chatRoom);
    }

    public ChatMessageSavedDto save(ChatMessageDto chatMessageDto, ChatRoom chatRoom, User user) {
        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageDto.getContent())
                .room(chatRoom)
                .sender(user)
                .build();

        chatMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageSavedDto.builder()
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .senderUsername(chatMessage.getSender().getVisibleUsername())
                .build();
    }
}
