package com.example.demo.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.entity.Chat;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chatList")
    public String chatList(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("chatList",  chatService.showAllChat(authentication.getName()));
        return "chatList";
    }

    @GetMapping("/{fromUser}/{toUser}")
    public String chat(@PathVariable String fromUser, @PathVariable String toUser, Model model, Authentication authentication) {
        /* 这是实际情况，但为了测试注释掉
        if(fromUser.equals(authentication.getName())) {
            model.addAttribute("fromUser",fromUser);
            model.addAttribute("toUser",toUser);
            return "chat";
        }
        return "redirect:/chat/chatList";
         */
        model.addAttribute("fromUser",fromUser);
        model.addAttribute("toUser",toUser);
        return "chat";
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
}
