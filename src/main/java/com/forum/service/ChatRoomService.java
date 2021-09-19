package com.forum.service;

import com.forum.dto.ChatRoomDto;
import com.forum.model.ChatMessage;
import com.forum.model.ChatRoom;
import com.forum.model.User;
import com.forum.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    public ChatRoom findByChatters(User chatter1, User chatter2) {
        Optional<ChatRoom> optionalChatRoom = Optional.empty();
        List<ChatRoom> potentialChatRooms = chatRoomRepository.findByChattersIn(Set.of(chatter1, chatter2));
        for (ChatRoom room: potentialChatRooms) {
            if (room.getChatters().containsAll(Set.of(chatter1, chatter2))) {
                optionalChatRoom = Optional.of(room);
            }
        }
        if (optionalChatRoom.isEmpty()) {
            ChatRoom chatRoom = ChatRoom.builder()
                                        .chatters(Set.of(chatter1, chatter2))
                                        .build();
            return chatRoomRepository.save(chatRoom);
        }
        return optionalChatRoom.get();
    }

    private List<ChatRoom> getAllChatRooms(User user) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByChattersIn(Set.of(user));
        return chatRooms;
    }

    public List<ChatRoomDto> getAllChat(User user) {
        List<ChatRoom> chatRooms = getAllChatRooms(user);
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom room: chatRooms) {
            if (chatMessageService.countMessages(room) <= 0) {
                continue;
            }
            HashSet<User> chatters = room.getChatters().stream().filter(user1 -> !user1.equals(user)).collect(Collectors.toCollection(HashSet::new));
            User recipient = chatters.stream().findFirst().get();
            ChatRoomDto chatRoomDto = ChatRoomDto
                    .builder()
                    .recipientId(String.valueOf(recipient.getId()))
                    .recipientUsername(recipient.getVisibleUsername())
                    .lastMessage(getLastChatMessage(room).getContent())
                    .build();
            chatRoomDtos.add(chatRoomDto);
        }

        return chatRoomDtos;
    }

    private ChatMessage getLastChatMessage(ChatRoom chatRoom) {
        return chatMessageService.getLastMessage(chatRoom);
    }

    public Optional<ChatRoom> getRoom(Long id) {
        return chatRoomRepository.findById(id);
    }
}
