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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageSavedDto> getMessagesFromChatRoom(ChatRoom chatRoom) {
        List<ChatMessage> messages = chatMessageRepository.findAllByRoom(chatRoom);
        return messages.stream().map(msg -> {
            return ChatMessageSavedDto
                    .builder()
                    .createdAt(msg.getCreatedAt())
                    .senderUsername(msg.getSender().getVisibleUsername())
                    .content(msg.getContent())
                    .build();
        }).collect(Collectors.toList());
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

    public Long countMessages(ChatRoom chatRoom) {
        return chatMessageRepository.countByRoom(chatRoom);
    }
}
