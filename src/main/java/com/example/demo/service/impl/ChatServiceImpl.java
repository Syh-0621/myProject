package com.example.demo.service.impl;

import com.example.demo.entity.Chat;
import com.example.demo.mapper.ChatMapper;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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

    @Override
    public String uploadImg(MultipartFile images, String username) {
        if (images.isEmpty())
            return null;
        String folderpath = "/home/syh/Pictures/img/";
        String filepath = username + "/" + new Random().nextInt(1000) + images.getOriginalFilename();
        File wholepath = new File(folderpath + filepath);
        if (!wholepath.getParentFile().exists()) {
            wholepath.getParentFile().mkdirs();
        }
        try {
            images.transferTo(wholepath);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "/images/" + filepath;
    }
}
