package com.example.demo.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.entity.Chat;
import com.example.demo.entity.ChatMsgVO;
import com.example.demo.entity.Product;
import com.example.demo.service.ChatMsgService;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMsgService chatMsgService;

    @GetMapping("/chatList")
    public String chatList(Authentication authentication, Model model) {
        List<Chat> chatList = chatService.showAllChat(authentication.getName());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("chatList",  chatList);
        return "chatList";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addChat(Chat chat) {
        System.out.println(chat);
        if (chatService.selectByChat(chat) != null)
            return "success";
        else if (chatService.addChat(chat) > 0) {
            return "success";
        }
        return "failed";
    }

    @PostMapping("/uploadImg")
    @ResponseBody
    public String uploadImg(@RequestParam("files") MultipartFile img, Authentication authentication) {
        return chatService.uploadImg(img, authentication.getName());
    }

    @PostMapping("/history")
    @ResponseBody
    public String history(@RequestBody JSONObject jsonObject) {
        System.out.println(JSON.toJSONString(chatMsgService.selectChatMsgByUser(jsonObject.getString("fromUser"), jsonObject.getString("toUser"))));
        return JSON.toJSONString(chatMsgService.selectChatMsgByUser(jsonObject.getString("fromUser"), jsonObject.getString("toUser")));
    }

    @PostMapping("/unread")
    @ResponseBody
    public Integer unread(@RequestBody JSONObject jsonObject) {
        return chatMsgService.getUnreadCount(jsonObject.getString("fromUser"), jsonObject.getString("toUser"));
    }
}
