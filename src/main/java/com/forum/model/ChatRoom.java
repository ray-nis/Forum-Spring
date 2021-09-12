package com.forum.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_chat_rooms", joinColumns = @JoinColumn(name = "chat_room_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> chatters;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<ChatMessage> messages;
}
