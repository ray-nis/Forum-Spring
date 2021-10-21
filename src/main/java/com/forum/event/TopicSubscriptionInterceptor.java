package com.forum.event;

import com.forum.model.ChatRoom;
import com.forum.model.User;
import com.forum.service.ChatRoomService;
import com.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TopicSubscriptionInterceptor implements ChannelInterceptor {

    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String idRequested = headerAccessor.getDestination().split("/")[2];
            User currentUser = userService.findUserByEmail(headerAccessor.getUser().getName()).get();
            Optional<ChatRoom> chatRoom = chatRoomService.getRoom(Long.valueOf(idRequested));
            if (chatRoom.isEmpty() || !chatRoom.get().getChatters().contains(currentUser)) {
                throw new AccessDeniedException("Access Denied");
            }
        }
        return message;
    }
}
