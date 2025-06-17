package com.example.demo.service;

import com.example.demo.entity.Chat;

import java.util.List;

public interface ChatService {
    List<Chat> showAllChat(String username);

    Chat selectByChat(Chat chat);

    int addChat(Chat chat);
}
