package com.example.demo.service.impl;

import com.example.demo.entity.ChatMsgVO;
import com.example.demo.mapper.ChatMsgMapper;
import com.example.demo.service.ChatMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Override
    public int insertChatMsg(ChatMsgVO chatMsg) {
        return chatMsgMapper.insert(chatMsg);
    }

    @Override
    public List<ChatMsgVO> selectChatMsgByUser(String MFromUser, String MToUser) {
        return chatMsgMapper.selectByUser(MFromUser, MToUser);
    }

    @Override
    public int updateChatMsg(ChatMsgVO chatMsg){
        return chatMsgMapper.update(chatMsg);
    }
}
