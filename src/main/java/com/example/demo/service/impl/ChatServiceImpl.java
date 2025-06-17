package com.example.demo.service.impl;

import com.example.demo.entity.Chat;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Override
    public List<Chat> showAllChat(String username) {
        return chatMapper.selectBySender(username);
    }

    @Override
    public Chat selectByChat(Chat chat){
        return chatMapper.selectByChat(chat);
    }

    @Override
    public int addChat(Chat chat){
        return chatMapper.add(chat);
    }
}
