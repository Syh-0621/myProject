package com.example.demo.service;

import com.example.demo.entity.Chat;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    List<Chat> showAllChat(String username);

    Chat selectByChat(Chat chat);

    int addChat(Chat chat);

    String uploadImg(MultipartFile[] images, String username);

    int deleteChatByPid(Integer pid);
}
