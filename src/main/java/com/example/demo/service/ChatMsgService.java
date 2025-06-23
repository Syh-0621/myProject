package com.example.demo.service;

import com.example.demo.entity.ChatMsgVO;

import java.util.List;

public interface ChatMsgService {
    int insertChatMsg(ChatMsgVO chatMsg);

    List<ChatMsgVO> selectChatMsgByUser(String MFromUser, String MToUser);

    int updateChatMsg(ChatMsgVO chatMsg);

    Integer getUnreadCount(String MFromUser, String MToUser);
}
