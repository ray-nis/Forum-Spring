package com.forum.service;

import com.forum.model.ChatRoom;
import com.forum.model.User;
import com.forum.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

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
}
