package com.forum.repository;

import com.forum.model.ChatMessage;
import com.forum.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoom(ChatRoom chatRoom);

    ChatMessage findFirstByRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    Long countByRoom(ChatRoom chatRoom);
}
