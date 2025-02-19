package com.fish.shareplan.domain.chat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_chat")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String content;
    private String type;

    // Getters, Setters, Constructor
}