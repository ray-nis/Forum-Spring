package com.forum.controller.v1;

import com.forum.dto.ChatMessageDto;
import com.forum.dto.ChatMessageSavedDto;
import com.forum.dto.ChatRoomDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.ChatMessage;
import com.forum.model.ChatRoom;
import com.forum.model.User;
import com.forum.service.ChatMessageService;
import com.forum.service.ChatRoomService;
import com.forum.service.UserService;
import com.forum.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @GetMapping("/chat")
    public String chat(Principal principal ,Model model) {
        User user = userService.findUserByEmail(principal.getName()).get();
        List<ChatRoomDto> chatRooms = chatRoomService.getAllChat(user);
        model.addAttribute("chatRooms", chatRooms);
        return "chat/chat";
    }

    @GetMapping("/chat/{id}")
    public String chat(@PathVariable("id") Long recipientId, Model model, Principal principal) throws ResourceNotFoundException {
        User user = userService.findUserByEmail(principal.getName()).get();
        if (user.getId() == recipientId) {
            return "redirect:/chat";
        }
        User otherUser = userService.findUserById(recipientId);
        ChatRoom chatRoom = chatRoomService.findByChatters(user, otherUser);
        List<ChatMessage> chatMessageList = chatMessageService.getMessagesFromChatRoom(chatRoom);
        model.addAttribute("visibleUsername", user.getVisibleUsername());
        model.addAttribute("chatId", chatRoom.getId());
        model.addAttribute("messages", chatMessageList);
        model.addAttribute("recipientId", recipientId);
        model.addAttribute("recipientUsername", otherUser.getVisibleUsername());
        return "chat/chatWithUser";
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) throws ResourceNotFoundException {
        User user = userService.findUserByEmail(principal.getName()).get();
        ChatRoom chatRoom = chatRoomService.findByChatters(user, userService.findUserById(chatMessageDto.getRecipientId()));
        ChatMessageSavedDto messageSavedDto = chatMessageService.save(chatMessageDto, chatRoom, user);
        messagingTemplate.convertAndSendToUser(String.valueOf(chatRoom.getId()), "/queue/messages", messageSavedDto);
    }
}
